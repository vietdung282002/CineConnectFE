package com.example.cineconnect.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentUserLikeFavouriteBinding

class UserLikeFavouriteFragment : Fragment() {
    private lateinit var fragmentUserLikeFavouriteBinding: FragmentUserLikeFavouriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserLikeFavouriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_like_favourite, container, false)
        return fragmentUserLikeFavouriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        fragmentUserLikeFavouriteBinding.ratingBar.rating = 0f
//
//        fragmentUserLikeFavouriteBinding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
//            fragmentUserLikeFavouriteBinding.ratingValue.text = "Rating: $rating"
//        }
    }

}