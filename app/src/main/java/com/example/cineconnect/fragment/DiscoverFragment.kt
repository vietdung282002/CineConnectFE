package com.example.cineconnect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.adapter.MovieListAdapter
import com.example.cineconnect.databinding.FragmentDiscoverBinding
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils.Companion.MOVIE_ID
import com.example.cineconnect.viewmodel.MovieViewModel


class DiscoverFragment : Fragment(),OnMovieClicked {
    private lateinit var fragmentDiscoverBinding: FragmentDiscoverBinding
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var fragmentManager: FragmentManager
    private val movieViewmodel: MovieViewModel by viewModels()
    private var movieList = listOf<MovieList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        fragmentDiscoverBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_discover, container, false
        )
        movieViewmodel.getMovieList()
        return fragmentDiscoverBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListAdapter = MovieListAdapter()
        movieListAdapter.setOnMovieListener(this)
        fragmentDiscoverBinding.lifecycleOwner = viewLifecycleOwner
        fragmentDiscoverBinding.rvMovie.adapter = movieListAdapter
        fragmentDiscoverBinding.rvMovie.setHasFixedSize(true)


        movieViewmodel.movieListResult.observe(viewLifecycleOwner){
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    submitList(it.data)
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


        fragmentManager = requireActivity().supportFragmentManager

    }

    override fun getOnMovieClicked(position: Int, movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID, movieId)

        val movieDetailFragment = MovieDetailFragment().apply {
            arguments = bundle
        }

        val containerId = (view?.parent as? ViewGroup)?.id
        if (containerId != null) {
            fragmentManager.beginTransaction()
                .add(containerId, movieDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showLoading() {
        fragmentDiscoverBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentDiscoverBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun submitList(response: MovieListResponse?){
        movieList = response?.movieLists!!
        movieListAdapter.submitList(movieList)
    }


}