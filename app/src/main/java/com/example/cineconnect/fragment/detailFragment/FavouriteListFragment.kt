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
import com.example.cineconnect.adapter.FavouritePagingAdapter
import com.example.cineconnect.databinding.FragmentFavouriteListBinding
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouriteListFragment : Fragment() {
    private lateinit var fragmentFavouriteListBinding: FragmentFavouriteListBinding
    private val favouriteAdapter = FavouritePagingAdapter()
    private val userViewModel: UserViewModel by viewModels()
    private var movieId: Int? = -1
    private var movieName: String? = ""
    private lateinit var fragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentFavouriteListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_favourite_list,
            container,
            false
        )
        arguments?.let {
            movieId = it.getInt(Utils.MOVIE_ID)
            movieName = it.getString(Utils.MOVIE_NAME)
        }
        movieId?.let {
            userViewModel.getFavourite(it)
        }
        return fragmentFavouriteListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = requireActivity().supportFragmentManager

        fragmentFavouriteListBinding.apply {
            backBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            tvMovieName.text = movieName
            rvFavourite.adapter = favouriteAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userFavouriteState.collectLatest { state ->
                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        state.data?.let { pagingData ->
                            favouriteAdapter.submitData(pagingData)
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
        fragmentFavouriteListBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentFavouriteListBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}