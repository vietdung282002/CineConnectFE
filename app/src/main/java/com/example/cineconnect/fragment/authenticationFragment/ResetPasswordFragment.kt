package com.example.cineconnect.fragment.authenticationFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentResetPasswordBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.viewmodel.UserViewModel


class ResetPasswordFragment : Fragment() {
    private lateinit var fragmentResetPasswordBinding: FragmentResetPasswordBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var fragmentManager: FragmentManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentResetPasswordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        return fragmentResetPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = requireActivity().supportFragmentManager

        fragmentResetPasswordBinding.apply {
            btnBack.setOnClickListener {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            btnContinue.setOnClickListener {
                if (etNewPassword.text.toString() == "") {
                    processError("Password cannot be empty")
                } else if (etConfirmNewPassword.text.toString() == "") {
                    processError("Confirm password cannot be empty")
                } else if (etNewPassword.text.toString() != etConfirmNewPassword.text.toString()) {
                    processError("Password does not match")
                } else {
                    userViewModel.resetPassword(etNewPassword.text.toString())
                }
            }
        }
        userViewModel.resetPasswordResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    backToSignIn()
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                    stopLoading()
                }

                else -> {
                    stopLoading()
                }
            }
        }
    }

    private fun backToSignIn() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private fun showLoading() {
        fragmentResetPasswordBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentResetPasswordBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}