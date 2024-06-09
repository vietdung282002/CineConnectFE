package com.example.cineconnect.fragment.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.adapter.CommentPagingAdapter
import com.example.cineconnect.databinding.FragmentReviewDetailBinding
import com.example.cineconnect.model.Review
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewDetailFragment : Fragment() {
    private lateinit var fragmentReviewDetailBinding: FragmentReviewDetailBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val commentAdapter = CommentPagingAdapter()
    private var reviewId: Int = -1
    private lateinit var fragmentManager: FragmentManager
    private var date: String = ""
    private var token: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentReviewDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review_detail, container, false)
        if (SessionManager.getToken(requireContext()) != null) {
            token = "Token " + SessionManager.getToken(requireContext())
        }
        arguments?.let {
            reviewId = it.getInt(Utils.REVIEW_ID)
            reviewViewModel.getReview(token, reviewId)
            reviewViewModel.getReviewCommentList(reviewId)
        }

        return fragmentReviewDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentManager = activity?.supportFragmentManager!!

        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val itemHeight = (screenHeight * (0.3)).toInt()

        val layoutParams = fragmentReviewDetailBinding.ivBackdropImage.layoutParams
        layoutParams.height = itemHeight

        fragmentReviewDetailBinding.ivBackdropImage.layoutParams = layoutParams

        reviewViewModel.likeState.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    fragmentReviewDetailBinding.likeBtn.setImageResource(R.drawable.baseline_favorite_24_red)
                } else {
                    fragmentReviewDetailBinding.likeBtn.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }

        reviewViewModel.numberOfLike.observe(viewLifecycleOwner) {
            fragmentReviewDetailBinding.tvNumberOfLike.text = it.toString()
        }

        reviewViewModel.reviewResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    response.data?.let { review -> updateUI(review) }
                }

                is BaseResponse.Error -> {
                    processError(response.msg)
                    stopLoading()
                }

                else -> {
                    stopLoading()
                }
            }
        }
        fragmentReviewDetailBinding.rvComment.adapter = commentAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            reviewViewModel.commentState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        state.data?.let { pagingData ->
                            commentAdapter.submitData(pagingData)
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

    private fun updateUI(review: Review) {
        val containerId = (view?.parent as? ViewGroup)?.id
        date = "Watched ${Utils.convertTime(review.watchedDay)}"
        fragmentReviewDetailBinding.apply {
            val movieBundle = Bundle()
            movieBundle.putInt(Utils.MOVIE_ID, review.movie.id)

            backBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            Glide.with(requireContext()).load(Utils.BACKDROP_LINK + review.movie.backdropPath)
                .placeholder(R.drawable.loading_image).error(R.drawable.try_later)
                .into(ivBackdropImage)
            Glide.with(requireContext()).load(Utils.POSTER_LINK + review.movie.posterPath)
                .placeholder(R.drawable.loading_image).error(R.drawable.try_later).into(posterImage)
            Glide.with(requireContext()).load(Utils.PROFILE_LINK + review.user.profilePic)
                .placeholder(R.drawable.loading_image).error(R.drawable.try_later).into(userImage)
            tvUserName.text = review.user.username
            llUser.setOnClickListener {

            }
            val movieInfo: String = if (review.movie.releaseDate != null) {
                "<font color='#FFFFFFFF'><b>${review.movie.title} </b></font> <font color='#9F9A9A'>${
                    review.movie.releaseDate?.substring(
                        0,
                        4
                    )
                }"
            } else {
                "<font color='#FFFFFFFF'><b>${review.movie.title} </b></font>"
            }
            tvMovieName.text = HtmlCompat.fromHtml(movieInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvMovieName.setOnClickListener {
                if (containerId != null) {
                    val movieDetailFragment = MovieDetailFragment().apply {
                        arguments = movieBundle
                    }
                    fragmentManager.beginTransaction()
                        .add(containerId, movieDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            posterImage.setOnClickListener {
                if (containerId != null) {
                    val movieDetailFragment = MovieDetailFragment().apply {
                        arguments = movieBundle
                    }
                    fragmentManager.beginTransaction()
                        .add(containerId, movieDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }

            if (review.favourite) {
                ivFavorite.visibility = View.VISIBLE
            }
            if (review.rating != 0f) {
                ratingBar.rating = review.rating
                ratingBar.visibility = View.VISIBLE
            }
            content.text = review.content
            tvNumberOfLike.text = review.likesCount.toString()
//            if (review.isLiked) {
//                likeBtn.setImageResource(R.drawable.baseline_favorite_24_red)
//            } else {
//                likeBtn.setImageResource(R.drawable.baseline_favorite_border_24)
//            }

            likeBtn.setOnClickListener {
                token?.let { it1 -> reviewViewModel.like(it1, review.id) }
            }

            watchedDay.text = date
        }
    }

    private fun showLoading() {
        fragmentReviewDetailBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentReviewDetailBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}