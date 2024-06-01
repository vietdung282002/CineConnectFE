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
import com.example.cineconnect.adapter.MoviePagingAdapter
import com.example.cineconnect.databinding.FragmentMovieSearchBinding
import com.example.cineconnect.fragment.detailFragment.MovieDetailFragment
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieSearchFragment(
    private val query: String,
    private val parentId: Int
) : Fragment(), OnMovieClicked {
    private lateinit var fragmentMovieSearchBinding: FragmentMovieSearchBinding
    private val movieAdapter = MoviePagingAdapter()
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieSearchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_search, container, false)
        movieViewModel.searchMovies(query)
        return fragmentMovieSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter.setOnMovieListener(this)
        fragmentMovieSearchBinding.rvMovie.adapter = movieAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.moviesState.collectLatest { state ->

                when (state) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        state.data?.let { pagingData ->
                            movieAdapter.submitData(pagingData)
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
        hideKeyboard()
        val bundle = Bundle()
        bundle.putInt(Utils.MOVIE_ID, movieId)

        val movieDetailFragment = MovieDetailFragment().apply {
            arguments = bundle
        }

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .add(parentId, movieDetailFragment)
            .addToBackStack(null)
            .commit()

    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}