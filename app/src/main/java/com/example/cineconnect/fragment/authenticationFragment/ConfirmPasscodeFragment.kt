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
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentConfirmPasscodeBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.viewmodel.UserViewModel


class ConfirmPasscodeFragment : Fragment() {
    private lateinit var fragmentConfirmPasscodeBinding: FragmentConfirmPasscodeBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var fragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentConfirmPasscodeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_passcode, container, false)
        return fragmentConfirmPasscodeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentManager = requireActivity().supportFragmentManager

        fragmentConfirmPasscodeBinding.apply {
            btnBack.setOnClickListener {
                fragmentManager.popBackStack()
                userViewModel.resetPasswordRequestResult.value = null
            }

            btnContinue.setOnClickListener {
                userViewModel.confirmPasscode(etPasscode.text.toString())
                goToResetPasswordFragment()
            }

            btnSendCodeAgain.setOnClickListener {
                userViewModel.resetPasswordRequest()
            }
        }

        userViewModel.resetPasswordRequestResult.observe(viewLifecycleOwner) {
            if (it is BaseResponse.Success) {
                process("Send code successfully")
            }
        }

        userViewModel.confirmPasscodeResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    goToResetPasswordFragment()
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

    private fun showLoading() {
        fragmentConfirmPasscodeBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentConfirmPasscodeBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun process(msg: String?) {
        showToast("$msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun goToResetPasswordFragment() {
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace<ResetPasswordFragment>(R.id.fragment_container_view)
            addToBackStack(null)
        }
    }

}