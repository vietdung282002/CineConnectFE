package com.example.cineconnect.fragment.authenticationFragment

import android.content.Intent
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
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.cineconnect.MainActivity
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentSignInBinding
import com.example.cineconnect.model.LoginResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.viewmodel.UserViewModel


class SignInFragment : Fragment() {
    private lateinit var fragmentSignInBinding: FragmentSignInBinding
    private lateinit var fragmentManager: FragmentManager
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSignInBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_in, container, false
        )

        return fragmentSignInBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = SessionManager.getToken(requireContext())

        if (!token.isNullOrBlank()) {
            navigateToHome()
        }

        fragmentManager = requireActivity().supportFragmentManager

        fragmentSignInBinding.apply {
            btnLogin.setOnClickListener {
                doLogin()
            }
            forgotPasswordBtn.setOnClickListener {

            }
            goToSignupBtn.setOnClickListener {
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<SignUpFragment>(R.id.fragment_container_view)
                    addToBackStack(null)
                }

            }
            forgotPasswordBtn.setOnClickListener {
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<RequestResetPasswordFragment>(R.id.fragment_container_view)
                    addToBackStack(null)
                }
            }
        }


        userViewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
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

    private fun doLogin() {
        val usernameOrEmail = fragmentSignInBinding.etUsernameOrEmail.text.toString()
        val password = fragmentSignInBinding.etPassword.text.toString()
        userViewModel.login(usernameOrEmail = usernameOrEmail, password = password)
    }

    private fun showLoading() {
        fragmentSignInBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentSignInBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processLogin(loginResponse: LoginResponse?) {
        if (!loginResponse?.data?.token.isNullOrEmpty()) {
            loginResponse?.data?.token?.let {
                SessionManager.saveAuthToken(requireContext(), it)
                SessionManager.getToken(requireContext())?.let { it1 -> Log.d("LOG_TAG_MAIN", it1) }
            }
        }
        if (!loginResponse?.data?.id.isNullOrEmpty()) {
            loginResponse?.data?.id?.let {
                SessionManager.saveUserId(requireContext(), it.toInt())
            }
        }
        navigateToHome()
    }

    private fun navigateToHome() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        activity?.finish()
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}