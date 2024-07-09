package com.example.cineconnect.view.fragment.bottomSheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentEditReviewBinding
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.ReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditReviewFragment : BottomSheetDialogFragment() {
    private lateinit var fragmentEditReviewBinding: FragmentEditReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels({ requireParentFragment().requireParentFragment() })
    private var token: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
//            view?.toString()?.let { it1 -> Log.d("LOG_TAG_MAIN", it1) }
//            Log.d("LOG_TAG_MAIN", view?.parent.toString())
            (view?.parent as ViewGroup).background = ColorDrawable(Color.TRANSPARENT)
        }
        return dialog
    }

    private fun setupFullscreen() {
        val dialog = dialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.peekHeight = 0 // Set peek height to 0 for full screen
        behavior.isDraggable = false
    }

    override fun onStart() {
        super.onStart()
        setupFullscreen()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentEditReviewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_review, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        reviewViewModel
        return fragmentEditReviewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentEditReviewBinding.viewModel = reviewViewModel
        fragmentEditReviewBinding.lifecycleOwner = viewLifecycleOwner

        fragmentEditReviewBinding.cancelBtn.setOnClickListener {
            dismiss()
        }

        fragmentEditReviewBinding.saveBtn.setOnClickListener {
            token?.let {
                reviewViewModel.editReview(
                    token!!,
                    fragmentEditReviewBinding.etReview.text.toString()
                )
            }
        }

        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val itemWidth = (screenWidth * 0.2).toInt()
        val itemHeight = (itemWidth * (1.5)).toInt()
        val posterLayoutParams = fragmentEditReviewBinding.posterImage.layoutParams
        posterLayoutParams.height = itemHeight
        posterLayoutParams.width = itemWidth

        fragmentEditReviewBinding.posterImage.layoutParams = posterLayoutParams
//        reviewViewModel.reviewContent.observe(viewLifecycleOwner) {
//            fragmentEditReviewBinding.etReview.setText(it)
//        }

        reviewViewModel.reviewResult.observe(viewLifecycleOwner) {
            if (it is BaseResponse.Success) {
                Glide.with(requireContext())
                    .load(Utils.Companion.POSTER_LINK + it.data?.movie?.posterPath)
                    .into(fragmentEditReviewBinding.posterImage)
                val htmlText =
                    "<font color='#FFFFFFFF'><b>${it.data?.movie?.title} </b></font> <font color='#9F9A9A'>${
                        it.data?.movie?.releaseDate?.substring(
                            0,
                            4
                        )
                    }"
                fragmentEditReviewBinding.tvMovieTitle.text =
                    HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }

        reviewViewModel.editContent.observe(viewLifecycleOwner) {
            fragmentEditReviewBinding.saveBtn.isClickable = it.isNotEmpty()
            if (fragmentEditReviewBinding.saveBtn.isClickable) {
                fragmentEditReviewBinding.saveBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            } else {
                fragmentEditReviewBinding.saveBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey2
                    )
                )
            }
        }

        reviewViewModel.editReviewResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    dismiss()
                    reviewViewModel.getReview(token, reviewViewModel.reviewId.value!!)
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
    }

    private fun showLoading() {
        fragmentEditReviewBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentEditReviewBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}