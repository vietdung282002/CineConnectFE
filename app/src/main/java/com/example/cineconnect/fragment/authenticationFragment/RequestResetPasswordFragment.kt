package com.example.cineconnect.fragment.authenticationFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentRequestResetPasswordBinding

class RequestResetPasswordFragment : Fragment() {
    private lateinit var fragmentRequestResetPasswordBinding: FragmentRequestResetPasswordBinding
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

    }


}