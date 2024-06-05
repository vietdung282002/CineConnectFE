package com.example.cineconnect.fragment.mainFragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.cineconnect.R
import com.example.cineconnect.adapter.MovieListAdapter
import com.example.cineconnect.databinding.FragmentDiscoverBinding
import com.example.cineconnect.fragment.detailFragment.MovieDetailFragment
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils.Companion.LOG_TAG_MAIN
import com.example.cineconnect.utils.Utils.Companion.MOVIE_ID
import com.example.cineconnect.viewmodel.MovieViewModel


class DiscoverFragment : Fragment(), OnMovieClicked {
    private lateinit var fragmentDiscoverBinding: FragmentDiscoverBinding
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var fragmentManager: FragmentManager
    private val movieViewmodel: MovieViewModel by viewModels()
    private var movieList = listOf<MovieList>()
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        fragmentDiscoverBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_discover, container, false
        )
        movieViewmodel.getMovieList(currentPage)
        return fragmentDiscoverBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListAdapter = MovieListAdapter()
        movieListAdapter.setOnMovieListener(this)
        fragmentDiscoverBinding.lifecycleOwner = viewLifecycleOwner
        fragmentDiscoverBinding.rvMovie.adapter = movieListAdapter
        fragmentDiscoverBinding.rvMovie.setHasFixedSize(true)


        movieViewmodel.movieListResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    Log.d(LOG_TAG_MAIN, "onViewCreated: ${it.data}")
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

    private fun updateUI(response: MovieListResponse?) {

        currentPage = response!!.currentPage
        totalPages = response.totalPages

        movieList = response.movieLists
        movieListAdapter.submitList(movieList)

        fragmentDiscoverBinding.paginationLayout.removeAllViews()

        addPrevButton()

        val maxVisiblePages = 7

        if (totalPages <= maxVisiblePages) {
            // Show all page numbers if total pages is less than or equal to max visible pages
            for (i in 1..totalPages) {
                addButton(i)
            }
        } else {
            // Show truncated page numbers
            if (currentPage <= 3) {
                for (i in 1..3) {
                    addButton(i)
                }
                addEllipsis()
                addButton(totalPages)
            } else if (currentPage >= totalPages - 2) {
                addButton(1)
                addEllipsis()
                for (i in totalPages - 2..totalPages) {
                    addButton(i)
                }
            } else {
                addButton(1)
                addEllipsis()
                for (i in currentPage - 1..currentPage + 1) {
                    addButton(i)
                }
                addEllipsis()
                addButton(totalPages)
            }
        }
        addNextButton()
    }

    private fun addButton(page: Int) {
        val button = Button(requireContext())
        button.text = page.toString()
        button.layoutParams
        button.textSize = 12f  // Set text size to be smaller
        button.setPadding(0, 0, 0, 0)
        button.setOnClickListener {
            currentPage = page
            movieViewmodel.getMovieList(currentPage)
        }
        if (page == currentPage) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.checked))
        } else {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
        }
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.07).toInt()
        val itemHeight = (itemWidth * (1))
        params.height = itemHeight
        params.width = itemWidth
        params.setMargins(8, 0, 8, 0)

        button.layoutParams = params

        fragmentDiscoverBinding.paginationLayout.addView(button)
    }

    private fun addEllipsis() {
        val textView = TextView(requireContext())
        textView.text = "..."
        textView.setOnClickListener {
            showGoToPageDialog()
        }
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        textView.setPadding(16, 0, 16, 0)
        fragmentDiscoverBinding.paginationLayout.addView(textView)
    }

    private fun addPrevButton() {
        val button = Button(requireContext())
        button.textSize = 12f
        button.setPadding(8, 4, 8, 4)
        button.setBackgroundResource(R.drawable.baseline_arrow_back_ios_24)
        button.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                movieViewmodel.getMovieList(currentPage)
            }
        }

        // Disable the Prev button if on the first page
        button.isEnabled = currentPage > 1

        // Set margin for the button
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.07).toInt()
        val itemHeight = (itemWidth * (1))
        params.height = itemHeight
        params.width = itemWidth
        params.setMargins(8, 0, 8, 0)
        button.layoutParams = params

        fragmentDiscoverBinding.paginationLayout.addView(button)
    }

    private fun addNextButton() {
        val button = Button(requireContext())
        button.setBackgroundResource(R.drawable.baseline_arrow_forward_ios_24)
        button.textSize = 12f
        button.setPadding(8, 4, 8, 4)

        button.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                movieViewmodel.getMovieList(currentPage)
            }
        }

        // Disable the Next button if on the last page
        button.isEnabled = currentPage < totalPages

        // Set margin for the button
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.07).toInt()
        val itemHeight = (itemWidth * (1))
        params.height = itemHeight
        params.width = itemWidth

        params.setMargins(8, 0, 8, 0)
        button.layoutParams = params

        fragmentDiscoverBinding.paginationLayout.addView(button)
    }

    private fun showGoToPageDialog() {
        val editText = EditText(requireContext())
        editText.inputType = InputType.TYPE_CLASS_NUMBER

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Go to Page")
            .setMessage("Enter page number:")
            .setView(editText)
            .setPositiveButton("Go") { _, _ ->
                val pageNumber = editText.text.toString().toIntOrNull()
                if (pageNumber != null && pageNumber in 1..totalPages) {
                    currentPage = pageNumber
                    movieViewmodel.getMovieList(currentPage)
                } else {
                    Toast.makeText(requireContext(), "Invalid page number", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }
}