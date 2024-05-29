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
import com.example.cineconnect.databinding.FragmentGenreDetailBinding
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.MovieViewModel

class GenreDetailFragment : Fragment(), OnMovieClicked {
    private lateinit var fragmentGenreDetailBinding: FragmentGenreDetailBinding
    private var genreId: Int? = -1
    private var genreName: String? = ""
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var fragmentManager: FragmentManager
    private var movieList = listOf<MovieList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentGenreDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_genre_detail, container, false)
        arguments?.let {
            genreId = it.getInt(Utils.GENRE_ID)
            genreName = it.getString(Utils.GENRE_NAME)
        }
        genreId?.let { movieViewModel.getMovieListByGenre(it) }
        return fragmentGenreDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentManager = requireActivity().supportFragmentManager

        fragmentGenreDetailBinding.tvGenreName.text = genreName
        fragmentGenreDetailBinding.backBtn.setOnClickListener {
            fragmentManager.popBackStack()
        }

        movieListAdapter = MovieListAdapter()
        movieListAdapter.setOnMovieListener(this)

        fragmentGenreDetailBinding.rvMovie.adapter = movieListAdapter
        movieViewModel.movieListResult.observe(viewLifecycleOwner) {
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

    }

    override fun getOnMovieClicked(position: Int, movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.MOVIE_ID, movieId)

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
        fragmentGenreDetailBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentGenreDetailBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun submitList(response: MovieListResponse?) {
        movieList = response?.movieLists!!
        movieListAdapter.submitList(movieList)
    }

}