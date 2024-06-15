package com.example.cineconnect.fragment.bottomSheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentMovieDetailBottomSheetBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.viewmodel.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MovieDetailBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var fragmentMovieDetailBottomSheetBinding: FragmentMovieDetailBottomSheetBinding
    private val movieViewModel: MovieViewModel by viewModels({ requireParentFragment() })
    private var token: String? = null
    private lateinit var addReviewFragment: AddReviewFragment

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
        behavior.peekHeight = 0
        behavior.isDraggable = false
    }

    override fun onStart() {
        super.onStart()
        setupFullscreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMovieDetailBottomSheetBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_movie_detail_bottom_sheet, container, false
        )
        if (SessionManager.getToken(requireContext()) != null) {
            token = "Token " + SessionManager.getToken(requireContext())
        }
        return fragmentMovieDetailBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMovieDetailBottomSheetBinding.viewModel = movieViewModel
        fragmentMovieDetailBottomSheetBinding.lifecycleOwner = viewLifecycleOwner

        movieViewModel.isWatched.observe(viewLifecycleOwner) {
            if (it) {
                fragmentMovieDetailBottomSheetBinding.apply {
                    watchIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources, R.drawable.watched, null
                        )
                    )
                    watchText.text = "Watched"
                }

            } else {
                fragmentMovieDetailBottomSheetBinding.apply {
                    watchIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources, R.drawable.not_watched, null
                        )
                    )
                    watchText.text = "Watch"
                }


            }
        }

        movieViewModel.isFavorite.observe(viewLifecycleOwner) {
            if (it) {
                fragmentMovieDetailBottomSheetBinding.apply {
                    likeIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources, R.drawable.baseline_favorite_60_red, null
                        )
                    )
                    likeText.text = "Liked"
                }
            } else {
                fragmentMovieDetailBottomSheetBinding.apply {
                    likeIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources, R.drawable.baseline_favorite_border_60, null
                        )
                    )
                    likeText.text = "Like"
                }
            }

        }

        movieViewModel.watchState.observe(viewLifecycleOwner) {
            if (it is BaseResponse.Error) {
                processError(it.msg)
            }
        }

        movieViewModel.favoriteState.observe(viewLifecycleOwner) {
            if (it is BaseResponse.Error) {
                processError(it.msg)
            }
        }

        fragmentMovieDetailBottomSheetBinding.watchIcon.setOnClickListener {
            token?.let { token -> movieViewModel.watch(token) }
        }
        fragmentMovieDetailBottomSheetBinding.likeIcon.setOnClickListener {
            token?.let { token -> movieViewModel.like(token) }
        }
        fragmentMovieDetailBottomSheetBinding.doneBtn.setOnClickListener {
            dismiss()
        }

        fragmentMovieDetailBottomSheetBinding.addReviewBtn.setOnClickListener {
            addReviewFragment = AddReviewFragment()
            addReviewFragment.show(childFragmentManager, addReviewFragment.tag)
        }

        fragmentMovieDetailBottomSheetBinding.notInterestBtn.setOnClickListener {

        }
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}