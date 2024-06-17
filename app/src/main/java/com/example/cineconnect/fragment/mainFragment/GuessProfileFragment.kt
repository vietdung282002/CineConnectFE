package com.example.cineconnect.fragment.mainFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cineconnect.R
import com.example.cineconnect.activity.AuthenticationActivity
import com.example.cineconnect.databinding.FragmentGuessProfileBinding

class GuessProfileFragment : Fragment() {
    private lateinit var fragmentGuessProfileBinding: FragmentGuessProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentGuessProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_guess_profile, container, false)
        return fragmentGuessProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentGuessProfileBinding.signUpBtn.setOnClickListener {
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }

}