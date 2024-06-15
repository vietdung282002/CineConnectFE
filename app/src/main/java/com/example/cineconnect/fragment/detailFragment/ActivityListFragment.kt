package com.example.cineconnect.fragment.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cineconnect.R
import com.example.cineconnect.adapter.ActivityPagingAdapter
import com.example.cineconnect.databinding.FragmentActivityListBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnActivityClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ActivityListFragment : Fragment(), OnActivityClicked {
    private lateinit var fragmentActivityListBinding: FragmentActivityListBinding
    private var userId: Int? = -1
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var fragmentManager: FragmentManager
    private val activityAdapter = ActivityPagingAdapter()
    private var containerId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentActivityListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_activity_list,
            container,
            false
        )
        arguments?.let {
            userId = it.getInt(Utils.USER_ID)
            userViewModel.getActivityList(userId!!)
        }


        return fragmentActivityListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = requireActivity().supportFragmentManager
        containerId = (view.parent as? ViewGroup)?.id!!

        activityAdapter.setOnActivityClicked(this)
        fragmentActivityListBinding.apply {
            backBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            rvActivity.adapter = activityAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.activityState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        state.data?.let { pagingData ->
                            activityAdapter.submitData(pagingData)
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
        fragmentActivityListBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentActivityListBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onReviewClicked(position: Int, review: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.REVIEW_ID, review)

        val reviewDetailFragment = ReviewDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(containerId, reviewDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onMovieClicked(position: Int, movie: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.MOVIE_ID, movie)

        val movieDetailFragment = MovieDetailFragment().apply {
            arguments = bundle
        }

        val containerId = (view?.parent as? ViewGroup)?.id
        if (containerId != null) {
            fragmentManager.beginTransaction()
                .replace(containerId, movieDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onUserClicked(position: Int, user: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.USER_ID, user)

        val userDetailFragment = UserDetailFragment().apply {
            arguments = bundle
        }
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(containerId, userDetailFragment)
            .addToBackStack(null)
            .commit()
    }

}