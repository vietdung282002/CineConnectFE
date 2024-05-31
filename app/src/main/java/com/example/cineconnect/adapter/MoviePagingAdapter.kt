package com.example.cineconnect.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.MoviePersonItemSearchBinding
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils

class MoviePagingAdapter : PagingDataAdapter<MovieList, MoviePagingAdapter.MovieViewHolder>(MovieComparator) {
    private var listener: OnMovieClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val adapterLayout =
            MoviePersonItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if(movie != null){
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
    }

    class MovieViewHolder(movieItemSearchBinding: MoviePersonItemSearchBinding) : RecyclerView.ViewHolder(movieItemSearchBinding.root) {
        val posterImage = movieItemSearchBinding.posterImage
        val tvContent = movieItemSearchBinding.tvContent
        val layout = movieItemSearchBinding.flItem
    }

    fun setOnMovieListener(listener: OnMovieClicked) {
        this.listener = listener
    }

    object MovieComparator : DiffUtil.ItemCallback<MovieList>() {
        override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem == newItem
        }
    }
}