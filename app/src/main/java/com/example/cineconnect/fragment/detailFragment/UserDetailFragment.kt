package com.example.cineconnect.fragment.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.adapter.ReviewPagingSearchAdapter
import com.example.cineconnect.databinding.FragmentUserDetailBinding
import com.example.cineconnect.model.User
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.onClickInterface.OnReviewClicked
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.ReviewViewModel
import com.example.cineconnect.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserDetailFragment : Fragment(), OnReviewClicked, OnMovieClicked {
    private lateinit var fragmentUserDetailBinding: FragmentUserDetailBinding
    private val userViewModel: UserViewModel by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()
    private var userId: Int = -1
    private var currentUserId: Int = -1
    private var token: String? = null
    private val reviewAdapter = ReviewPagingSearchAdapter()
    private lateinit var fragmentManager: FragmentManager
    private var containerId: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, container, false)
        arguments?.let {
            userId = it.getInt(Utils.USER_ID)
        }
        token = "Token " + SessionManager.getToken(requireContext())

        if (userId != -1 && userId != SessionManager.getUserId(requireContext())!!) {
            userViewModel.getUser(token, userId)
            reviewViewModel.getReviewListByUser(userId)
        } else {
            currentUserId = SessionManager.getUserId(requireContext())!!
            if (currentUserId != -1) {
                userViewModel.getUser(token, currentUserId)
                reviewViewModel.getReviewListByUser(currentUserId)
            }
        }
        return fragmentUserDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = activity?.supportFragmentManager!!

        containerId = (view.parent as? ViewGroup)?.id!!


        userViewModel.userResult.observe(viewLifecycleOwner) { response ->

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
        fragmentUserDetailBinding.rvReview.adapter = reviewAdapter
        reviewAdapter.setOnReviewListener(this)
        reviewAdapter.setOnMovieListener(this)
        viewLifecycleOwner.lifecycleScope.launch {
            reviewViewModel.reviewState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        state.data?.let { pagingData ->
                            reviewAdapter.submitData(pagingData)
                        }
                    }

                    is BaseResponse.Error -> {
                        stopLoading()
                        processError(state.msg)
                    }
                }
            }
        }
    }

    private fun updateUI(user: User) {

        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val itemHeight = (screenHeight * (0.15)).toInt()

        val layoutParams = fragmentUserDetailBinding.profilePic.layoutParams
        layoutParams.height = itemHeight
        layoutParams.width = itemHeight

        fragmentUserDetailBinding.profilePic.layoutParams = layoutParams

        val userBundle = Bundle()
        userBundle.putInt(Utils.USER_ID, user.id)

        fragmentUserDetailBinding.apply {
            tvUsername.text = user.username
            tvFollowers.text = user.followerCount.toString() + " Followers"
            tvFollowing.text = user.followingCount.toString() + " Following"

            Glide.with(requireContext()).load(Utils.USER_PROFILE_LINK + user.profilePic)
                .into(profilePic)

            activityLayout.setOnClickListener { }
            favouriteLayout.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(Utils.USER_ID, user.id)
                bundle.putString(Utils.TITLE, user.username + "'s Favourites")
                bundle.putInt(Utils.TYPE, 2)

                val genreDetailFragment = MovieListFragment().apply {
                    arguments = bundle
                }

                fragmentManager.beginTransaction()
                    .replace(containerId, genreDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
            filmsLayout.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(Utils.USER_ID, user.id)
                bundle.putString(Utils.TITLE, user.username + "'s Watched")
                bundle.putInt(Utils.TYPE, 3)

                val genreDetailFragment = MovieListFragment().apply {
                    arguments = bundle
                }

                fragmentManager.beginTransaction()
                    .replace(containerId, genreDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }

            tvFollowers.setOnClickListener {
                val followerFragment = FollowerFragment().apply {
                    arguments = userBundle
                }
                fragmentManager.beginTransaction().replace(containerId, followerFragment)
                    .addToBackStack(null).commit()
            }
            tvFollowing.setOnClickListener {
                val followingFragment = FollowingFragment().apply {
                    arguments = userBundle
                }
                fragmentManager.beginTransaction().replace(containerId, followingFragment)
                    .addToBackStack(null).commit()
            }

            favouriteCount.text = user.favouriteCount.toString()
            filmsCount.text = user.watchedCount.toString()

            if (user.favouriteCount > 0) {
                favouriteDetailIcon.visibility = View.VISIBLE
            }
            if (user.watchedCount > 0) {
                filmsDetailIcon.visibility = View.VISIBLE
            }

            if (user.bio != "") {
                tvBio.text = user.bio
                tvBio.visibility = View.VISIBLE
            } else {
                tvBio.visibility = View.GONE
            }
            backBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            if (userId != -1 && userId != SessionManager.getUserId(requireContext())!!) {
                settingBtn.visibility = View.GONE
                followBtn.visibility = View.VISIBLE
                followBtn.setOnClickListener {
                    userViewModel.follow(token, userId)
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
            } else {
//                settingBtn.visibility = View.VISIBLE
//                settingBtn.setOnClickListener{
//
//
//                    settingFragment = BottomSheetFragment()
//                    settingFragment.setBottomSheetCloseListener(this@UserDetailFragment)
//                    settingFragment.show(fragmentManager, settingFragment.tag)
//                }
                followBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun showLoading() {
        fragmentUserDetailBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentUserDetailBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun getOnReviewClicked(position: Int, reviewId: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.REVIEW_ID, reviewId)

        val reviewDetailFragment = ReviewDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(containerId, reviewDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun getOnMovieClicked(position: Int, movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.MOVIE_ID, movieId)

        val movieDetailFragment = MovieDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(containerId, movieDetailFragment)
            .addToBackStack(null)
            .commit()
    }

}