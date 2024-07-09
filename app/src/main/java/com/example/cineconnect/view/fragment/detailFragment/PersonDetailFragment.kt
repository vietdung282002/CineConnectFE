package com.example.cineconnect.view.fragment.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.databinding.FragmentPersonDetailBinding
import com.example.cineconnect.model.entities.Person
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.utils.Utils.Companion.PERSON_ID
import com.example.cineconnect.utils.Utils.Companion.PROFILE_LINK
import com.example.cineconnect.view.adapter.MovieListAdapter
import com.example.cineconnect.view.onClickInterface.OnMovieClicked
import com.example.cineconnect.viewmodel.PersonViewModel

class PersonDetailFragment : Fragment(), OnMovieClicked {
    private lateinit var personDetailBinding: FragmentPersonDetailBinding
    private val personViewModel: PersonViewModel by viewModels()
    private var isExpanded = false
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var fragmentManager: FragmentManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        personDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_person_detail, container, false)
        arguments?.let {
            val personId = it.getInt(PERSON_ID)
            personViewModel.getPerson(personId)
        }
        return personDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.35).toInt()
        val itemHeight = (itemWidth * (1.5)).toInt()

        val layoutParams = personDetailBinding.personImage.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemHeight

        personDetailBinding.personImage.layoutParams = layoutParams
        personDetailBinding.biographyText.maxHeight = itemHeight

        movieListAdapter = MovieListAdapter()
        movieListAdapter.setOnMovieListener(this)

        fragmentManager = requireActivity().supportFragmentManager

        personViewModel.personResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    response.data?.let { person -> showDetail(person, itemHeight) }
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

    private fun showDetail(person: Person, itemHeight: Int) {
        personDetailBinding.apply {
            tvPersonName.text = person.name
            Glide.with(requireContext()).load(PROFILE_LINK + person.profilePath).into(personImage)
            biographyText.text = person.biography
            tvNumberOfMovies.text = person.movies?.size.toString()
            biographyText.setOnClickListener {
                if (isExpanded) {
                    biographyText.maxHeight = itemHeight
                    gradientView.visibility = View.VISIBLE
                } else {
                    biographyText.maxHeight = Int.MAX_VALUE
                    gradientView.visibility = View.GONE
                }
                isExpanded = !isExpanded
            }
            rvMovie.adapter = movieListAdapter
            movieListAdapter.submitList(person.movies)
            rvMovie.setHasFixedSize(true)
            backBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
        }

    }

    private fun showLoading() {
        personDetailBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        personDetailBinding.progressBarLayout.visibility = View.GONE
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
        val containerId = (view?.parent as? ViewGroup)?.id
        if (containerId != null) {
            fragmentManager.beginTransaction()
                .replace(containerId, movieDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}