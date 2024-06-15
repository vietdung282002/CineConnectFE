package com.example.cineconnect.fragment.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineconnect.R
import com.example.cineconnect.adapter.ReviewPagingAdapter
import com.example.cineconnect.databinding.FragmentReviewListOfMovieBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnReviewClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.utils.Utils.Companion.REVIEW_ID
import com.example.cineconnect.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ReviewListOfMovieFragment : Fragment(), OnReviewClicked {
    private lateinit var fragmentReviewListOfMovieBinding: FragmentReviewListOfMovieBinding
    private var movieId: Int? = -1
    private var movieName: String? = ""
    private val reviewViewModel: ReviewViewModel by viewModels()
    private lateinit var fragmentManager: FragmentManager
    private val reviewAdapter = ReviewPagingAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentReviewListOfMovieBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_review_list_of_movie,
            container,
            false
        )
        arguments?.let {
            movieId = it.getInt(Utils.MOVIE_ID)
            movieName = it.getString(Utils.MOVIE_NAME)
        }
        movieId?.let {
            reviewViewModel.getReviewList(it)
        }
        return fragmentReviewListOfMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = requireActivity().supportFragmentManager
        reviewAdapter.setOnReviewListener(this)
        fragmentReviewListOfMovieBinding.apply {
            backBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            tvMovieName.text = movieName
            rvReview.adapter = reviewAdapter
        }
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

    private fun showLoading() {
        fragmentReviewListOfMovieBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentReviewListOfMovieBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun getOnReviewClicked(position: Int, reviewId: Int) {
        val bundle = Bundle()
        bundle.putInt(REVIEW_ID, reviewId)
        val reviewDetailFragment = ReviewDetailFragment().apply {
            arguments = bundle
        }
        val containerId = (view?.parent as? ViewGroup)?.id
        if (containerId != null) {
            fragmentManager.beginTransaction()
                .replace(containerId, reviewDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }


}