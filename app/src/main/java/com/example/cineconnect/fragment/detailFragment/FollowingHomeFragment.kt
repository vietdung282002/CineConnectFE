package com.example.cineconnect.fragment.detailFragment

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
import com.example.cineconnect.adapter.ReviewPagingSearchAdapter
import com.example.cineconnect.databinding.FragmentFollowingHomeBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.onClickInterface.OnReviewClicked
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FollowingHomeFragment(private val parentId: Int) : Fragment(), OnReviewClicked,
    OnMovieClicked {
    private lateinit var fragmentFollowingHomeBinding: FragmentFollowingHomeBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val reviewAdapter = ReviewPagingSearchAdapter()
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentFollowingHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_following_home, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        reviewViewModel.getNewsFeedReviewList(token!!)
        return fragmentFollowingHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewAdapter.setOnReviewListener(this)
        reviewAdapter.setOnMovieListener(this)
        fragmentFollowingHomeBinding.rvReview.adapter = reviewAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            reviewViewModel.reviewState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
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
            .add(parentId, reviewDetailFragment)
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
            .add(parentId, movieDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showLoading() {
        fragmentFollowingHomeBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentFollowingHomeBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}