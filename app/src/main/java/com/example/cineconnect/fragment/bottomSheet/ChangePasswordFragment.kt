package com.example.cineconnect.fragment.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentChangePasswordBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ChangePasswordFragment : Fragment() {
    private lateinit var fragmentChangePasswordBinding: FragmentChangePasswordBinding
    private val userViewModel: UserViewModel by viewModels(
        { requireParentFragment().requireParentFragment() }
    )
    private var token: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentChangePasswordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        return fragmentChangePasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentChangePasswordBinding.viewModel = userViewModel
        fragmentChangePasswordBinding.lifecycleOwner = viewLifecycleOwner

        fragmentChangePasswordBinding.apply {
            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            doneBtn.setOnClickListener {
                token?.let { token -> userViewModel.changePassword(token) }
            }
        }

        userViewModel.updatePasswordResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    (parentFragment as? BottomSheetDialogFragment)?.dismiss()
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
        fragmentChangePasswordBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentChangePasswordBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}