package com.example.cineconnect.fragment.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.adapter.MovieSearchListAdapter
import com.example.cineconnect.databinding.FragmentMovieSearchBinding
import com.example.cineconnect.fragment.MovieDetailFragment
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.utils.Utils.Companion.LOG_TAG
import com.example.cineconnect.viewmodel.MovieViewModel

class MovieSearchFragment(
    private val query: String,
    private val parentId: Int
) : Fragment(), OnMovieClicked {
    private lateinit var fragmentMovieSearchBinding: FragmentMovieSearchBinding
    private lateinit var movieSearchListAdapter: MovieSearchListAdapter
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieSearchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_search, container, false)
        movieViewModel.getSearchMovie(1, query)

        return fragmentMovieSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieSearchListAdapter = MovieSearchListAdapter()
        movieSearchListAdapter.setOnMovieListener(this)
        fragmentMovieSearchBinding.rvMovie.adapter = movieSearchListAdapter

        movieViewModel.movieListResult.observe(viewLifecycleOwner) {
            Log.d(LOG_TAG, "onViewCreated: $it")
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    updateUI(it.data)
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

    private fun updateUI(response: MovieListResponse?) {
        movieSearchListAdapter.submitList(response?.movieLists)
    }


    private fun showLoading() {
        fragmentMovieSearchBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentMovieSearchBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun getOnMovieClicked(position: Int, movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.MOVIE_ID, movieId)

        val movieDetailFragment = MovieDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        if (parentId != null) {
            fragmentManager.beginTransaction()
                .add(parentId, movieDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }


}