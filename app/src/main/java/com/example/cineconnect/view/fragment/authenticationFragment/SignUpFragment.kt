package com.example.cineconnect.view.fragment.authenticationFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.Utils
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            updateSubmitButtonState()
        }
    }

    private fun updateSubmitButtonState() {
        fragmentSignUpBinding.apply {
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            btnSignup.isEnabled =
                email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()

            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                btnSignup.setBackgroundResource(R.drawable.button_bg)
            } else {
                btnSignup.setBackgroundResource(R.drawable.button_bg2)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = requireActivity().supportFragmentManager

        fragmentSignUpBinding.apply {
            etEmail.addTextChangedListener(textWatcher)
            etUsername.addTextChangedListener(textWatcher)
            etPassword.addTextChangedListener(textWatcher)

            goToLoginBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            btnSignup.setOnClickListener {
                if (!Utils.isValidEmail(etEmail.text.toString())) {
                    processError("email invalid")
                } else if (!Utils.isPasswordValid(etPassword.text.toString())) {
                    processError("password invalid")
                } else {
                    doRegister()
                }
            }
        }

        userViewModel.registerResult.observe(viewLifecycleOwner) {
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