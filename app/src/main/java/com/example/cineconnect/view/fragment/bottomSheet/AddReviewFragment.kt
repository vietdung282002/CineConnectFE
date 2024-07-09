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
import com.example.cineconnect.databinding.FragmentAddReviewBinding
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddReviewFragment : BottomSheetDialogFragment() {
    private lateinit var fragmentAddReviewBinding: FragmentAddReviewBinding
    private val movieViewModel: MovieViewModel by viewModels({ requireParentFragment().requireParentFragment() })
    private var token: String? = null

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
        fragmentAddReviewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_review, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        return fragmentAddReviewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddReviewBinding.viewModel = movieViewModel
        fragmentAddReviewBinding.lifecycleOwner = viewLifecycleOwner

        fragmentAddReviewBinding.cancelBtn.setOnClickListener {
            dismiss()
        }

        fragmentAddReviewBinding.saveBtn.setOnClickListener {
            token?.let { it1 -> movieViewModel.addReview(it1) }
        }

        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val itemWidth = (screenWidth * 0.2).toInt()
        val itemHeight = (itemWidth * (1.5)).toInt()
        val posterLayoutParams = fragmentAddReviewBinding.posterImage.layoutParams
        posterLayoutParams.height = itemHeight
        posterLayoutParams.width = itemWidth

        fragmentAddReviewBinding.posterImage.layoutParams = posterLayoutParams

        movieViewModel.movieResult.observe(viewLifecycleOwner) {
            if (it is BaseResponse.Success) {
                Glide.with(requireContext()).load(Utils.Companion.POSTER_LINK + it.data?.posterPath)
                    .into(fragmentAddReviewBinding.posterImage)
                val htmlText =
                    "<font color='#FFFFFFFF'><b>${it.data?.title} </b></font> <font color='#9F9A9A'>${
                        it.data?.releaseDate?.substring(
                            0,
                            4
                        )
                    }"
                fragmentAddReviewBinding.tvMovieTitle.text =
                    HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }

        movieViewModel.reviewContent.observe(viewLifecycleOwner) {
            fragmentAddReviewBinding.saveBtn.isClickable = it.isNotEmpty()
            if (fragmentAddReviewBinding.saveBtn.isClickable) {
                fragmentAddReviewBinding.saveBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            } else {
                fragmentAddReviewBinding.saveBtn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey2
                    )
                )
            }
        }

        movieViewModel.reviewResult.observe(viewLifecycleOwner) {
            if (it is BaseResponse.Success) {
                dismiss()
            } else if (it is BaseResponse.Error) {
                processError(it.msg)
            }
        }
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}