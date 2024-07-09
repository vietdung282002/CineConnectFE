package com.example.cineconnect.view.fragment.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentReviewSearchBinding
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.view.adapter.ReviewPagingSearchAdapter
import com.example.cineconnect.view.fragment.detailFragment.MovieDetailFragment
import com.example.cineconnect.view.fragment.detailFragment.ReviewDetailFragment
import com.example.cineconnect.view.onClickInterface.OnMovieClicked
import com.example.cineconnect.view.onClickInterface.OnReviewClicked
import com.example.cineconnect.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewSearchFragment(

) : Fragment(), OnReviewClicked, OnMovieClicked {
    private lateinit var fragmentReviewSarchBinding: FragmentReviewSearchBinding
    private val reviewAdapter = ReviewPagingSearchAdapter()
    private val reviewViewModel: ReviewViewModel by viewModels()
    private var parentId = -1
    private var query = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentReviewSarchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review_search, container, false)
        arguments?.let {
            parentId = it.getInt(Utils.CONTAINER_ID)
            query = it.getString(Utils.QUERY, "")
        }
        reviewViewModel.getSearchReviewList(query)

        return fragmentReviewSarchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewAdapter.setOnReviewListener(this)
        reviewAdapter.setOnMovieListener(this)
        fragmentReviewSarchBinding.rvReview.adapter = reviewAdapter
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
        hideKeyboard()
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
        hideKeyboard()
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
        fragmentReviewSarchBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentReviewSarchBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}