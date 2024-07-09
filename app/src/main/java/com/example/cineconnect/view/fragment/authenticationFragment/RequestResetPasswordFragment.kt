package com.example.cineconnect.view.fragment.authenticationFragment

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
import com.example.cineconnect.databinding.FragmentRequestResetPasswordBinding
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.UserViewModel

class RequestResetPasswordFragment : Fragment() {
    private lateinit var fragmentRequestResetPasswordBinding: FragmentRequestResetPasswordBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var fragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRequestResetPasswordBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_request_reset_password,
            container,
            false
        )
        return fragmentRequestResetPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = requireActivity().supportFragmentManager


        fragmentRequestResetPasswordBinding.viewModel = userViewModel
        fragmentRequestResetPasswordBinding.lifecycleOwner = viewLifecycleOwner

        fragmentRequestResetPasswordBinding.apply {
            btnContinue.setOnClickListener {
                if (etEmail.text.toString() != "" && Utils.isValidEmail(etEmail.text.toString())) {
                    userViewModel.resetPasswordRequest()
                }
            }
            btnBack.setOnClickListener {
                fragmentManager.popBackStack()
            }
        }

        userViewModel.resetPasswordRequestResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    goToConfirmation()
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
        fragmentRequestResetPasswordBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentRequestResetPasswordBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun goToConfirmation() {
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace<ConfirmPasscodeFragment>(R.id.fragment_container_view)
            addToBackStack(null)
        }
    }

}