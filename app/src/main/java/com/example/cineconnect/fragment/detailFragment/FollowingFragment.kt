package com.example.cineconnect.fragment.detailFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineconnect.R
import com.example.cineconnect.adapter.UserPagingAdapter
import com.example.cineconnect.databinding.FragmentFollowingBinding
import com.example.cineconnect.fragment.mainFragment.ProfileFragment
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnFollowButtonClicked
import com.example.cineconnect.onClickInterface.OnUserClicked
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FollowingFragment : Fragment(), OnUserClicked, OnFollowButtonClicked {
    private lateinit var fragmentFollowingBinding: FragmentFollowingBinding
    private val userViewModel: UserViewModel by viewModels()
    private val userAdapter = UserPagingAdapter()
    private var token: String? = null
    private var userId: Int = -1
    private var containerId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentFollowingBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        arguments?.let {
            userId = it.getInt(Utils.USER_ID)
            userViewModel.getFollowingUser(token, userId)
        }
        return fragmentFollowingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        containerId = (view.parent as? ViewGroup)?.id!!

        userAdapter.setOnUserListener(this)
        userAdapter.setOnFollowButtonListener(this)
        fragmentFollowingBinding.rvFollowing.adapter = userAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        state.data?.let { pagingData ->
                            userAdapter.submitData(pagingData)
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

    private fun showLoading() {
        fragmentFollowingBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentFollowingBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun getOnUserClicked(position: Int, userId: Int) {
        hideKeyboard()
        val bundle = Bundle()
        bundle.putInt(Utils.USER_ID, userId)

        val userProfile = ProfileFragment().apply {
            arguments = bundle
        }
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(containerId, userProfile)
            .addToBackStack(null)
            .commit()
    }

    override fun getOnFollowButtonClicked(position: Int, userId: Int) {
        userViewModel.follow(token!!, userId)
        userViewModel.followStatus.observe(viewLifecycleOwner) { (userId, isFollowing) ->
            userAdapter.snapshot().items.find { it.id == userId }?.let {
                it.isFollowing = isFollowing
                userAdapter.notifyItemChanged(position)
            }

        }
    }

}