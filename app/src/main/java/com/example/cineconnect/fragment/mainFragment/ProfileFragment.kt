package com.example.cineconnect.fragment.mainFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentProfileBinding
import com.example.cineconnect.fragment.detailFragment.FollowerFragment
import com.example.cineconnect.fragment.detailFragment.FollowingFragment
import com.example.cineconnect.model.User
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private val userViewModel: UserViewModel by viewModels()
    private var userId: Int = -1
    private var currentUser: Int = -1
    private var token: String? = null
//    private lateinit var settingFragment: SettingFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        arguments?.let {
            userId = it.getInt(Utils.USER_ID)
        }
        token = "Token " + SessionManager.getToken(requireContext())

        if(userId != -1){
            userViewModel.getUser(token,userId)
        }else{
            currentUser = SessionManager.getUserId(requireContext())!!
            if (currentUser != -1) {
                userViewModel.getUser(token,currentUser.toInt())
            }
        }
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userResult.observe(viewLifecycleOwner){response ->
            Log.d("LOG_TAG_MAIN", "user: $response")

            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    response.data?.let { user -> updateUI(user) }
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

    private fun updateUI(user: User) {

        val containerId = (view?.parent as? ViewGroup)?.id

        val fragmentManager = activity?.supportFragmentManager

        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val itemHeight = (screenHeight * (0.15)).toInt()

        val layoutParams = fragmentProfileBinding.profilePic.layoutParams
        layoutParams.height = itemHeight
        layoutParams.width = itemHeight

        fragmentProfileBinding.profilePic.layoutParams = layoutParams

        val userBundle = Bundle()
        userBundle.putInt(Utils.USER_ID, user.id)

        fragmentProfileBinding.apply {
            tvUsername.text = user.username
            tvFollowers.text = user.followerCount.toString() + " Followers"
            tvFollowing.text = user.followingCount.toString() + " Following"

            Glide.with(requireContext()).load(Utils.PROFILE_LINK + user.profilePic).into(profilePic)

            activityLayout.setOnClickListener {  }
            favouriteLayout.setOnClickListener {  }
            filmsLayout.setOnClickListener {  }

            tvFollowers.setOnClickListener {
                if (containerId != null) {
                    val followerFragment = FollowerFragment().apply {
                        arguments = userBundle
                    }
                    fragmentManager?.beginTransaction()?.add(containerId, followerFragment)
                        ?.addToBackStack(null)?.commit()
                }
            }
            tvFollowing.setOnClickListener {
                if (containerId != null) {
                    val followingFragment = FollowingFragment().apply {
                        arguments = userBundle
                    }
                    fragmentManager?.beginTransaction()?.add(containerId, followingFragment)
                        ?.addToBackStack(null)?.commit()
                }
            }

            favouriteCount.text = user.favouriteCount.toString()
            filmsCount.text = user.watchedCount.toString()

            if(user.favouriteCount > 0){
                favouriteDetailIcon.visibility = View.VISIBLE
            }
            if(user.watchedCount > 0){
                filmsDetailIcon.visibility = View.VISIBLE
            }

            if(user.bio != "" && user.bio != null){
                tvBio.text = user.bio
                tvBio.visibility = View.VISIBLE
            }else{
                tvBio.visibility = View.GONE
            }
            if(userId != -1){
                settingBtn.visibility = View.GONE
                followBtn.visibility = View.VISIBLE
                followBtn.setOnClickListener {
                    userViewModel.follow(token, userId)
                }
                backBtn.visibility = View.VISIBLE
                backBtn.setOnClickListener{
                    fragmentManager?.popBackStack()
                }
                userViewModel.followStatus.observe(viewLifecycleOwner) { (_, isFollowing) ->
                    if (isFollowing == true) {
                        followBtn.text = "Following"
                        followBtn.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.following_btn)
                        followBtn.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )

                    } else {
                        followBtn.text = "Follow"
                        followBtn.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.follow_btn)
                        followBtn.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )

                    }
                }
            }else{
                settingBtn.visibility = View.VISIBLE
                settingBtn.setOnClickListener{
//                    settingFragment = SettingFragment()
//                    if (fragmentManager != null) {
//                        settingFragment.show(fragmentManager,settingFragment.tag)
//                    }
                }
                followBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun showLoading() {
        fragmentProfileBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentProfileBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}