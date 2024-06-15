package com.example.cineconnect.fragment.bottomSheet

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentReviewOptionBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.DeleteReviewListener
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.viewmodel.ReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ReviewOptionFragment : BottomSheetDialogFragment() {
    private lateinit var fragmentReviewOptionBinding: FragmentReviewOptionBinding
    private val reviewViewModel: ReviewViewModel by viewModels({ requireParentFragment() })
    private lateinit var editReviewFragment: EditReviewFragment
    private var token: String? = null
    private var deleteReviewListener: DeleteReviewListener? = null

    fun setDeleteReviewListener(listener: DeleteReviewListener) {
        deleteReviewListener = listener
    }

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentReviewOptionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review_option, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        return fragmentReviewOptionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentReviewOptionBinding.apply {
            editReviewBtn.setOnClickListener {
                editReviewFragment = EditReviewFragment()
                editReviewFragment.show(childFragmentManager, editReviewFragment.tag)
            }

            deleteReviewBtn.setOnClickListener {
                showAlertDialogButtonClicked()
            }

            doneBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun showAlertDialogButtonClicked() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")

        val customLayout: View = layoutInflater.inflate(R.layout.alert_layout, null)
        builder.setView(customLayout)

        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
            token?.let { reviewViewModel.deleteReview(it) }
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_progress, null)
            val progressDialogBuilder = AlertDialog.Builder(requireContext())
            progressDialogBuilder.setView(dialogView)
            progressDialogBuilder.setCancelable(false)

            val progressDialog = progressDialogBuilder.create()
            progressDialog.show()

            reviewViewModel.editReviewResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is BaseResponse.Loading -> {
                        progressDialog.show()
                    }

                    is BaseResponse.Success -> {
                        progressDialog.dismiss()
                        AlertDialog.Builder(requireContext())
                            .setTitle("Success")
                            .setMessage("Review deleted successfully.")
                            .setPositiveButton("OK") { _, _ -> }
                            .show()
                        this.dismiss()
                        deleteReviewListener?.onDeleted()

                    }

                    else -> {
                        progressDialog.dismiss()
                        AlertDialog.Builder(requireContext())
                            .setTitle("Error")
                            .setMessage("Failed to delete review.")
                            .setPositiveButton("OK") { _, _ -> }
                            .show()
                    }
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->

        }
        val dialog = builder.create()
        dialog.show()
    }

}