package com.example.cineconnect.view.fragment.bottomSheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentEditCommentBinding
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.ReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EditCommentFragment : BottomSheetDialogFragment() {
    private lateinit var fragmentEditCommentBinding: FragmentEditCommentBinding
    private val reviewViewModel: ReviewViewModel by viewModels({ requireParentFragment() })
    private var token: String? = null
    private var commentId: Int = -1
    private var reviewId: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
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
        fragmentEditCommentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_comment, container, false)
        arguments?.let {
            commentId = it.getInt(Utils.COMMENT_ID)
            reviewId = it.getInt(Utils.REVIEW_ID)
            reviewViewModel.getComment(commentId)
        }
        token = "Token " + SessionManager.getToken(requireContext())
        return fragmentEditCommentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentEditCommentBinding.viewModel = reviewViewModel
        fragmentEditCommentBinding.lifecycleOwner = viewLifecycleOwner

        fragmentEditCommentBinding.cancelBtn.setOnClickListener {
            dismiss()
        }

        fragmentEditCommentBinding.saveBtn.setOnClickListener {
            Log.d(
                "LOG_TAG_MAIN",
                "onViewCreated: ${fragmentEditCommentBinding.etReview.text.toString()} $token"
            )
            token?.let { it1 ->
                reviewViewModel.editComment(
                    it1,
                    fragmentEditCommentBinding.etReview.text.toString(),
                    commentId,
                    reviewId
                )
            }
        }
        reviewViewModel.editCommentContent.observe(viewLifecycleOwner) {
            fragmentEditCommentBinding.saveBtn.isClickable = it.isNotEmpty()
            if (fragmentEditCommentBinding.saveBtn.isClickable) {
                fragmentEditCommentBinding.saveBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            } else {
                fragmentEditCommentBinding.saveBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey2
                    )
                )
            }
        }

        reviewViewModel.editCommentResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    dismiss()
                    reviewViewModel.getReviewCommentList(reviewId)
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
        fragmentEditCommentBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentEditCommentBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}