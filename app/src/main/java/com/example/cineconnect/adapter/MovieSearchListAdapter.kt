package com.example.cineconnect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.MovieItemSearchBinding
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils

class MovieSearchListAdapter :
    ListAdapter<MovieList, MovieSearchListAdapter.MovieViewHolder>(MovieListAdapter.MovieItemDiffUtils()) {
    private var listener: OnMovieClicked? = null

    class MovieViewHolder(movieItemSearchBinding: MovieItemSearchBinding) :
        RecyclerView.ViewHolder(movieItemSearchBinding.root) {
        val posterImage = movieItemSearchBinding.posterImage
        val tvContent = movieItemSearchBinding.tvContent
        val layout = movieItemSearchBinding.flItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val adapterLayout =
            MovieItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(adapterLayout)
    }

    override fun submitList(list: List<MovieList>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        val director = movie.directors?.get(0)

        val displayMetrics = holder.itemView.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.2).toInt()
        val itemHeight = (itemWidth * (1.5)).toInt()
        val layoutParams = holder.posterImage.layoutParams

        layoutParams.width = itemWidth
        layoutParams.height = itemHeight
        holder.posterImage.layoutParams = layoutParams

        Glide.with(holder.itemView.context).load(Utils.POSTER_LINK + movie.posterPath)
            .into(holder.posterImage)
        val htmlText =
            "<font color='#FFFFFFFF'><b>${movie.originalTitle} </b></font> <font color='#9F9A9A'>${
                movie.releaseDate?.substring(
                    0,
                    4
                )
            }, directed by </font> <font color='#FFFFFFFF'>" +
                    "<b>${director?.name} </b></font>"
        holder.tvContent.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.layout.setOnClickListener {
            listener?.getOnMovieClicked(position, movie.id)
        }
    }

    fun setOnMovieListener(listener: OnMovieClicked) {
        this.listener = listener
    }

}