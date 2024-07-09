package com.example.cineconnect.view.fragment.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentRecommendHomeBinding
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.view.adapter.ReviewPagingSearchAdapter
import com.example.cineconnect.view.onClickInterface.OnMovieClicked
import com.example.cineconnect.view.onClickInterface.OnReviewClicked
import com.example.cineconnect.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecommendHomeFragment() : Fragment(), OnReviewClicked,
    OnMovieClicked {
    private lateinit var fragmentRecommendHomeBinding: FragmentRecommendHomeBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val reviewAdapter = ReviewPagingSearchAdapter()
    private var token: String? = null
    private var parentId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRecommendHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recommend_home, container, false)
        arguments?.let {
            parentId = it.getInt(Utils.CONTAINER_ID)
        }
        token = "Token " + SessionManager.getToken(requireContext())
        reviewViewModel.getRecommendReviewList(token!!)
        return fragmentRecommendHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewAdapter.setOnReviewListener(this)
        reviewAdapter.setOnMovieListener(this)

        fragmentRecommendHomeBinding.container.setOnRefreshListener {
            reviewViewModel.getRecommendReviewList(token!!)
        }

        fragmentRecommendHomeBinding.rvReview.adapter = reviewAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            reviewViewModel.reviewState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        fragmentRecommendHomeBinding.container.isRefreshing = false

                        state.data?.let { pagingData ->
                            reviewAdapter.submitData(pagingData)
                        }
                    }

                    is BaseResponse.Error -> {
                        stopLoading()
                        processError(state.msg)
                    }
                }
            }
        }
    }

    override fun getOnReviewClicked(position: Int, reviewId: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.REVIEW_ID, reviewId)

        val reviewDetailFragment = ReviewDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(parentId, reviewDetailFragment)
            .addToBackStack(null)
            .commit()

    }

    override fun getOnMovieClicked(position: Int, movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.MOVIE_ID, movieId)

        val movieDetailFragment = MovieDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(parentId, movieDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showLoading() {
        fragmentRecommendHomeBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentRecommendHomeBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}