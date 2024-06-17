package com.example.cineconnect.fragment.detailFragment

import android.app.AlertDialog
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
import com.example.cineconnect.fragment.bottomSheet.EditCommentFragment
import com.example.cineconnect.fragment.bottomSheet.ReviewOptionFragment
import com.example.cineconnect.model.Review
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.DeleteReviewListener
import com.example.cineconnect.onClickInterface.OptionsMenuClickListener
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.ReviewViewModel
import com.example.cineconnect.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewDetailFragment : Fragment(), DeleteReviewListener, OptionsMenuClickListener {
    private lateinit var fragmentReviewDetailBinding: FragmentReviewDetailBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val commentAdapter = CommentPagingAdapter()
    private var reviewId: Int = -1
    private lateinit var fragmentManager: FragmentManager
    private var date: String = ""
    private var token: String? = null
    private var userId: Int = -1
    private lateinit var reviewOptionFragment: ReviewOptionFragment
    private lateinit var editCommentFragment: EditCommentFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentReviewDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review_detail, container, false)
        if (SessionManager.getToken(requireContext()) != null) {
            token = "Token " + SessionManager.getToken(requireContext())
        }
        userId = SessionManager.getUserId(requireContext())!!
        val arguments = arguments
        arguments?.let {
            reviewId = it.getInt(Utils.REVIEW_ID)
            reviewViewModel.getReview(token, reviewId)
            reviewViewModel.getReviewCommentList(reviewId)
        }
        userViewModel.getUser(token, userId)
        return fragmentReviewDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentManager = activity?.supportFragmentManager!!

        fragmentReviewDetailBinding.viewModel = reviewViewModel
        fragmentReviewDetailBinding.lifecycleOwner = viewLifecycleOwner

        fragmentReviewDetailBinding.container.setOnRefreshListener {
            reviewViewModel.getReview(token, reviewId)
            reviewViewModel.getReviewCommentList(reviewId)
        }
        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        val itemHeight = (screenHeight * (0.3)).toInt()
        val backdropLayoutParams = fragmentReviewDetailBinding.ivBackdropImage.layoutParams
        backdropLayoutParams.height = itemHeight

        fragmentReviewDetailBinding.ivBackdropImage.layoutParams = backdropLayoutParams

        val itemWidth = (screenWidth * (0.08)).toInt()
        val userImageLayoutParams = fragmentReviewDetailBinding.currentUserImage.layoutParams
        userImageLayoutParams.width = itemWidth
        userImageLayoutParams.height = itemWidth

        fragmentReviewDetailBinding.currentUserImage.layoutParams = userImageLayoutParams
        fragmentReviewDetailBinding.userImage.layoutParams = userImageLayoutParams

        reviewViewModel.likeState.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    fragmentReviewDetailBinding.likeBtn.setImageResource(R.drawable.baseline_thumb_up_alt_24)
                } else {
                    fragmentReviewDetailBinding.likeBtn.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
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
                    fragmentReviewDetailBinding.container.isRefreshing = false

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
        commentAdapter.setOptionsMenuClickListener(this)
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


        reviewViewModel.comment.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                fragmentReviewDetailBinding.sendBtn.visibility = View.VISIBLE
            } else {
                fragmentReviewDetailBinding.sendBtn.visibility = View.GONE
            }
        }

        reviewViewModel.commentResult.observe(viewLifecycleOwner) {
            if (it is BaseResponse.Success) {
                reviewViewModel.getReviewCommentList(reviewId)
            } else if (it is BaseResponse.Error) {
                processError(it.msg)
            }
        }

        fragmentReviewDetailBinding.sendBtn.setOnClickListener {
            if (fragmentReviewDetailBinding.sendBtn.isClickable) {
                token?.let { token -> reviewViewModel.comment(token, reviewId) }
            }
        }

        userViewModel.profilePicture.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(Utils.USER_PROFILE_LINK + it)
                .placeholder(R.drawable.loading_image).error(R.drawable.try_later)
                .into(fragmentReviewDetailBinding.currentUserImage)
        }

        reviewOptionFragment = ReviewOptionFragment()

    }

    private fun updateUI(review: Review) {
        val containerId = (view?.parent as? ViewGroup)?.id
        date = "Watched on ${Utils.convertTime(review.watchedDay)}"

        if (token == null) {
            fragmentReviewDetailBinding.layoutComment.visibility = View.GONE
            fragmentReviewDetailBinding.likeBtn.isClickable = false
        }
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
            Glide.with(requireContext()).load(Utils.USER_PROFILE_LINK + review.user.profilePic)
                .placeholder(R.drawable.loading_image).error(R.drawable.try_later).into(userImage)
            val htmlText =
                "<font color='#FFFFFFFF'>${review.user.username} </font>  <font color='#9F9A9A'>${
                    Utils.getRelativeTime(review.timeStamp)
                }"
            tvUserName.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            llUser.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(Utils.USER_ID, userId)

                val userDetailFragment = UserDetailFragment().apply {
                    arguments = bundle
                }
                val fragmentManager = requireActivity().supportFragmentManager
                if (containerId != null) {
                    fragmentManager.beginTransaction()
                        .replace(containerId, userDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            val movieInfo: String = if (review.movie.releaseDate != null) {
                "<font color='#FFFFFFFF'><b>${review.movie.title} </b></font> <font color='#9F9A9A'>${
                    review.movie.releaseDate?.substring(
                        0, 4
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
                    fragmentManager.beginTransaction().replace(containerId, movieDetailFragment)
                        .addToBackStack(null).commit()
                }
            }
            posterImage.setOnClickListener {
                if (containerId != null) {
                    val movieDetailFragment = MovieDetailFragment().apply {
                        arguments = movieBundle
                    }
                    fragmentManager.beginTransaction().replace(containerId, movieDetailFragment)
                        .addToBackStack(null).commit()
                }
            }

            if (review.favourite) {
                ivFavorite.visibility = View.VISIBLE
            }
            if (review.rating != 0f) {
                ratingBar.rating = review.rating
                ratingBar.visibility = View.VISIBLE
            }
            tvNumberOfLike.text = review.likesCount.toString()

            likeBtn.setOnClickListener {
                token?.let { it1 -> reviewViewModel.like(it1, review.id) }
            }

            watchedDay.text = date
            if (review.user.id == userId) {
                moreBtn.visibility = View.VISIBLE
                moreBtn.setOnClickListener {
                    reviewOptionFragment.setDeleteReviewListener(this@ReviewDetailFragment)
                    reviewOptionFragment.show(childFragmentManager, reviewOptionFragment.tag)
                }
            }
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

    override fun onDeleted() {
        fragmentManager.popBackStack()
    }

    override fun onDeleteClicked(position: Int, commentId: Int) {
        token?.let { reviewViewModel.deleteComment(it, commentId) }
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_progress, null)
        val progressDialogBuilder = AlertDialog.Builder(requireContext())
        progressDialogBuilder.setView(dialogView)
        progressDialogBuilder.setCancelable(false)

        val progressDialog = progressDialogBuilder.create()
        progressDialog.show()

        reviewViewModel.editCommentResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    progressDialog.show()
                }

                is BaseResponse.Success -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Success")
                        .setMessage("comment deleted successfully.")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()

                    reviewViewModel.getReviewCommentList(reviewId)

                }

                else -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("Failed to delete comment.")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()
                }
            }
        }
    }

    override fun onEditClicked(position: Int, commentId: Int) {
        editCommentFragment = EditCommentFragment()
        val bundle = Bundle()
        bundle.putInt(Utils.COMMENT_ID, commentId)
        bundle.putInt(Utils.REVIEW_ID, reviewId)
        editCommentFragment.arguments = bundle
        editCommentFragment.show(childFragmentManager, editCommentFragment.tag)
    }

}