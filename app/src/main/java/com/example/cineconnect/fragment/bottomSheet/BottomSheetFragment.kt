package com.example.cineconnect.fragment.bottomSheet

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentBottomSheetBinding
import com.example.cineconnect.onClickInterface.BottomSheetListener
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var fragmentBottomSheetBinding: FragmentBottomSheetBinding
    private val userViewModel: UserViewModel by viewModels()
    private var currentUserId: Int = -1
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        bottomSheetCloseListener?.onBottomSheetDismissed()
    }

    private var token: String? = null

    private var bottomSheetCloseListener: BottomSheetListener? = null

    fun setBottomSheetCloseListener(listener: BottomSheetListener) {
        bottomSheetCloseListener = listener
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
        fragmentBottomSheetBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        currentUserId = SessionManager.getUserId(requireContext())!!
        userViewModel.getUser(token, currentUserId)
        return fragmentBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentContainerView = fragmentBottomSheetBinding.fragmentContainer
        childFragmentManager.commit {
            replace(R.id.fragmentContainer, SettingFragment())
        }
    }


}