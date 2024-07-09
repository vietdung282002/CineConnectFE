package com.example.cineconnect.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.ReviewSearchBinding
import com.example.cineconnect.model.entities.ReviewList
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.view.onClickInterface.OnMovieClicked
import com.example.cineconnect.view.onClickInterface.OnReviewClicked

class ReviewPagingSearchAdapter :
    PagingDataAdapter<ReviewList, ReviewPagingSearchAdapter.ReviewViewHolder>(ReviewComparator) {
    class ReviewViewHolder(reviewSearchBinding: ReviewSearchBinding) :
        RecyclerView.ViewHolder(reviewSearchBinding.root) {
        val movieInfo = reviewSearchBinding.tvMovieInfo
        val favourite = reviewSearchBinding.ivFavorite
        val rating = reviewSearchBinding.ratingBar
        val username = reviewSearchBinding.tvUsername
        val profile = reviewSearchBinding.profileImage
        val poster = reviewSearchBinding.posterImage
        val content = reviewSearchBinding.content
        val layout = reviewSearchBinding.flItem
        val again = reviewSearchBinding.ivAgain
    }

    private var onReviewClicked: OnReviewClicked? = null
    private var onMovieClicked: OnMovieClicked? = null


    object ReviewComparator : DiffUtil.ItemCallback<ReviewList>() {
        override fun areItemsTheSame(oldItem: ReviewList, newItem: ReviewList): Boolean {
            return oldItem.user == newItem.user
        }

        override fun areContentsTheSame(oldItem: ReviewList, newItem: ReviewList): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnReviewListener(listener: OnReviewClicked) {
        this.onReviewClicked = listener
    }

    fun setOnMovieListener(listener: OnMovieClicked) {
        this.onMovieClicked = listener
    }
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth * 0.08).toInt()

            val layoutParams = holder.profile.layoutParams

            layoutParams.width = itemWidth
            layoutParams.height = itemWidth

            holder.profile.layoutParams = layoutParams

            holder.username.text = review.user.username
            Glide.with(holder.itemView.context)
                .load(Utils.USER_PROFILE_LINK + review.user.profilePic)
                .into(holder.profile)
            Glide.with(holder.itemView.context).load(Utils.POSTER_LINK + review.movie.posterPath)
                .into(holder.poster)
            val htmlText =
                "<font color='#FFFFFFFF'><b>${review.movie.title} </b></font> <font color='#9F9A9A'>${
                    review.movie.releaseDate?.substring(
                        0,
                        4
                    )
                }"
            holder.movieInfo.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            if (review.favourite) {
                holder.favourite.visibility = View.VISIBLE
            }
            if (review.again) {
                holder.again.visibility = View.VISIBLE
            }
            if (review.rating > 0) {
                holder.rating.rating = review.rating.toFloat()
                holder.rating.visibility = View.VISIBLE
            }
            holder.content.text = review.content
            holder.layout.setOnClickListener {
                onReviewClicked?.getOnReviewClicked(position, review.id)
            }
            holder.poster.setOnClickListener {
                onMovieClicked?.getOnMovieClicked(position, review.movie.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val adapterLayout =
            ReviewSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(adapterLayout)
    }
}