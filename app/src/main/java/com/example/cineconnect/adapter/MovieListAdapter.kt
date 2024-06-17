package com.example.cineconnect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.MovieItemBinding
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.onClickInterface.OnMovieClicked
import com.example.cineconnect.utils.Utils.Companion.POSTER_LINK

class MovieListAdapter :
    ListAdapter<MovieList, MovieListAdapter.MovieViewHolder>(MovieItemDiffUtils()) {
    private var listener: OnMovieClicked? = null

    class MovieViewHolder(movieItemBinding: MovieItemBinding) :
        RecyclerView.ViewHolder(movieItemBinding.root) {
        val poster: ImageView = movieItemBinding.posterImage
    }

    class MovieItemDiffUtils : DiffUtil.ItemCallback<MovieList>() {
        override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
            return oldItem == newItem
        }

    }

    override fun submitList(list: List<MovieList>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val adapterLayout =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        val displayMetrics = holder.itemView.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.29).toInt()
        val itemHeight = (itemWidth * (1.5)).toInt()

        val layoutParams = holder.itemView.layoutParams

        layoutParams.width = itemWidth
        layoutParams.height = itemHeight
        holder.itemView.layoutParams = layoutParams

        Glide.with(holder.itemView.context).load(POSTER_LINK + movie.posterPath).into(holder.poster)

        holder.poster.setOnClickListener {
            listener?.getOnMovieClicked(position, movie.id)
        }
    }

    fun setOnMovieListener(listener: OnMovieClicked) {
        this.listener = listener
    }
}