package com.example.cineconnect.fragment.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineconnect.R
import com.example.cineconnect.adapter.UserPagingAdapter
import com.example.cineconnect.databinding.FragmentUserSearchBinding
import com.example.cineconnect.fragment.detailFragment.UserDetailFragment
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnFollowButtonClicked
import com.example.cineconnect.onClickInterface.OnUserClicked
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserSearchFragment(
    private val query: String,
    private val parentId: Int
) : Fragment(), OnUserClicked, OnFollowButtonClicked {
    private lateinit var fragmentUserSearchBinding: FragmentUserSearchBinding
    private val userAdapter = UserPagingAdapter()
    private val userViewModel: UserViewModel by viewModels()
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserSearchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_search, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        userViewModel.getSearchUser(token,query)
        return fragmentUserSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter.setOnUserListener(this)
        userAdapter.setOnFollowButtonListener(this)
        fragmentUserSearchBinding.rvUser.adapter = userAdapter
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
        fragmentUserSearchBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentUserSearchBinding.progressBarLayout.visibility = View.GONE
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

        val userDetailFragment = UserDetailFragment().apply {
            arguments = bundle
        }
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(parentId, userDetailFragment)
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