package com.example.cineconnect.fragment.detailFragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.adapter.ViewPagerAdapter
import com.example.cineconnect.databinding.FragmentMovieDetailBinding
import com.example.cineconnect.model.Movie
import com.example.cineconnect.model.Rating
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.utils.Utils.Companion.BACKDROP_LINK
import com.example.cineconnect.utils.Utils.Companion.LOG_TAG_MAIN
import com.example.cineconnect.utils.Utils.Companion.MOVIE_ID
import com.example.cineconnect.utils.Utils.Companion.MOVIE_NAME
import com.example.cineconnect.utils.Utils.Companion.POSTER_LINK
import com.example.cineconnect.viewmodel.MovieViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class MovieDetailFragment : Fragment() {
    private lateinit var fragmentMovieDetailBinding: FragmentMovieDetailBinding
    private lateinit var barChart: BarChart
    private lateinit var barData: BarData
    private lateinit var barDataSet: BarDataSet
    private lateinit var vpAdapter: ViewPagerAdapter
    private var isExpanded = false
    private var movieId: Int = -1
    private val movieViewmodel: MovieViewModel by viewModels()
    private var containerId = -1
    private lateinit var fragmentManager: FragmentManager
    private var token: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMovieDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        token = "Token " + SessionManager.getToken(requireContext())

        arguments?.let {
            movieId = it.getInt(MOVIE_ID)
            movieViewmodel.getMovie(token,movieId)
        }

        return fragmentMovieDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerId = (view.parent as? ViewGroup)?.id!!

        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.35).toInt()
        val itemHeight = (itemWidth * (1.5)).toInt()

        val layoutParams = fragmentMovieDetailBinding.moviePoster.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemHeight

        fragmentMovieDetailBinding.moviePoster.layoutParams = layoutParams

        fragmentManager = activity?.supportFragmentManager!!


        movieViewmodel.movieResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    response.data?.let { movie -> updateUI(movie) }
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

    private fun updateUI(movieObj: Movie) {
        val directorObj = movieObj.directors[0]
        val ratingObj = movieObj.rating[0]
        val castList = movieObj.casts.toList()
        val genreList = movieObj.genres.toList()

        fragmentMovieDetailBinding.apply {
            collapsingToolbar.title = movieObj.title

            Glide.with(this@MovieDetailFragment).load(BACKDROP_LINK + movieObj.backdropPath)
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.try_later).into(ivBackdrop)
            backBtn.setOnClickListener {
                fragmentManager.popBackStack()
            }
            moreBtn.setOnClickListener {

            }
            tvReleaseDate.text = movieObj.releaseDate.take(4)
            tvDirectorName.text = directorObj.name
            tvDirectorName.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(Utils.PERSON_ID, directorObj.id)

                val personDetailFragment = PersonDetailFragment().apply {
                    arguments = bundle
                }

                val containerId = (view?.parent as? ViewGroup)?.id
                if (containerId != null) {
                    fragmentManager.beginTransaction()
                        .add(containerId, personDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }

            }
            tvRuntime.text = movieObj.runtime.toString() + " mins"
            Glide.with(this@MovieDetailFragment)
                .load(POSTER_LINK + movieObj.posterPath)
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.try_later)
                .into(moviePoster)
            tvTagline.text = movieObj.tagline
            overviewText.text = movieObj.overview
            overviewText.setOnClickListener {
                if (isExpanded) {
                    overviewText.maxLines = 3
                    gradientView.visibility = View.VISIBLE
                } else {
                    overviewText.maxLines = Integer.MAX_VALUE
                    gradientView.visibility = View.GONE
                }
                isExpanded = !isExpanded
            }

            val barEntriesList = createBarEntriesList(ratingObj)
            drawChart(barEntriesList)

            tvNumberOfLikes.apply {
                text = movieObj.favouriteCount.toString()
            }
            tvNumberOfReviews.apply {
                text = movieObj.reviewCount.toString()
            }

            tvAvrRating.text = ratingObj.avr.rateAvg.toString()

            likedList.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(MOVIE_ID, movieObj.id)
                bundle.putString(MOVIE_NAME, movieObj.title)

                val favouriteListFragment = FavouriteListFragment().apply {
                    arguments = bundle
                }
                fragmentManager.beginTransaction()
                    .add(containerId, favouriteListFragment)
                    .addToBackStack(null)
                    .commit()
            }

            reviewsList.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(MOVIE_ID, movieObj.id)
                bundle.putString(MOVIE_NAME, movieObj.title)

                val reviewListOfMovieFragment = ReviewListOfMovieFragment().apply {
                    arguments = bundle
                }
                fragmentManager.beginTransaction()
                    .add(containerId, reviewListOfMovieFragment)
                    .addToBackStack(null)
                    .commit()
            }


        }
        vpAdapter = activity?.let { ViewPagerAdapter(it, castList, genreList, containerId) }!!

        val viewPager = fragmentMovieDetailBinding.viewPager
        viewPager.adapter = vpAdapter
        val tabBar = fragmentMovieDetailBinding.tabBar
        tabBar.attachTo(viewPager)
    }

    private fun showLoading() {
        fragmentMovieDetailBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentMovieDetailBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun drawChart(barEntriesList: ArrayList<BarEntry>) {
        barChart = fragmentMovieDetailBinding.BarChart
        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")
        barDataSet.setDrawValues(false)
        barDataSet.barShadowColor = Color.TRANSPARENT
        barData = BarData(barDataSet)
        barData.barWidth = 0.4f
        barChart.data = barData

        barDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.orange))


        barDataSet.valueTextSize = 16f

        barChart.setFitBars(true)

        barChart.setDrawValueAboveBar(false)
        barChart.description.isEnabled = false
        barChart.axisRight.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisRight.setDrawAxisLine(false)
        barChart.axisRight.axisMinimum = 0f
        barChart.axisLeft.setDrawAxisLine(false)
        barChart.axisLeft.axisMinimum = 0f
        barChart.xAxis.setDrawAxisLine(true)
        barChart.legend.isEnabled = false
        barChart.setTouchEnabled(false)
        barChart.xAxis.isEnabled = false
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.invalidate()


    }

    private fun createBarEntriesList(rating: Rating): ArrayList<BarEntry> {
        val barEntriesList = ArrayList<BarEntry>()

        for ((ratingValue, count) in rating.rating) {
            val adjustedCount = if (count == 0) 0.01f else count.toFloat()
            barEntriesList.add(BarEntry(ratingValue.toFloat(), adjustedCount))
        }

        return barEntriesList
    }


}