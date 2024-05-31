package com.example.cineconnect.fragment.authenticationFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentSignUpBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.viewmodel.UserViewModel

class SignUpFragment : Fragment() {
    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding
    private lateinit var fragmentManager: FragmentManager
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSignUpBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up, container, false
        )

        // Inflate the layout for this fragment
        return fragmentSignUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = requireActivity().supportFragmentManager

        fragmentSignUpBinding.apply {
            goToLoginBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            btnSignup.setOnClickListener {
                doRegister()
            }
        }

        userViewModel.registerResult.observe(viewLifecycleOwner) {
            Log.d("TAG", "onViewCreated: $it")
            when (it) {
                is BaseResponse.Success -> {
                    stopLoading()
                    fragmentManager.popBackStack()
                }

                is BaseResponse.Loading -> {
                    showLoading()
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

    private fun doRegister() {
        fragmentSignUpBinding.apply {
            userViewModel.register(
                username = etUsername.text.toString(),
                email = etEmail.text.toString(),
                password = etPassword.text.toString()
            )
        }
    }

    private fun showLoading() {
        fragmentSignUpBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentSignUpBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}