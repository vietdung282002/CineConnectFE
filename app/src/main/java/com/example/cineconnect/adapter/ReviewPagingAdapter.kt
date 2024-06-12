package com.example.cineconnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.ReviewItemBinding
import com.example.cineconnect.model.ReviewList
import com.example.cineconnect.onClickInterface.OnReviewClicked
import com.example.cineconnect.utils.Utils

class ReviewPagingAdapter :
    PagingDataAdapter<ReviewList, ReviewPagingAdapter.ReviewViewHolder>(ReviewComparator) {
    private var onReviewClicked: OnReviewClicked? = null

    class ReviewViewHolder(reviewItemBinding: ReviewItemBinding) :
        RecyclerView.ViewHolder(reviewItemBinding.root) {
        val layout = reviewItemBinding.layoutMain
        val username = reviewItemBinding.tvUsername
        val favourite = reviewItemBinding.ivFavorite
        val rating = reviewItemBinding.ratingBar
        val content = reviewItemBinding.content
        val profileImage = reviewItemBinding.profileImage
    }

    object ReviewComparator : DiffUtil.ItemCallback<ReviewList>() {
        override fun areItemsTheSame(oldItem: ReviewList, newItem: ReviewList): Boolean {
            return oldItem.user == newItem.user
        }

        override fun areContentsTheSame(oldItem: ReviewList, newItem: ReviewList): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth * 0.08).toInt()

            val layoutParams = holder.profileImage.layoutParams

            layoutParams.width = itemWidth
            layoutParams.height = itemWidth

            holder.profileImage.layoutParams = layoutParams
            holder.username.text = review.user.username
            if (review.rating.toFloat() != 0f) {
                holder.rating.rating = review.rating.toFloat()
                holder.rating.visibility = View.VISIBLE
            }

            if (review.favourite) {
                holder.favourite.visibility = View.VISIBLE
            }
            holder.content.text = review.content
            Glide.with(holder.itemView).load(Utils.USER_PROFILE_LINK + review.user.profilePic)
                .into(holder.profileImage)
            holder.layout.setOnClickListener {
                onReviewClicked?.getOnReviewClicked(position, review.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val adapterLayout =
            ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(adapterLayout)
    }

    fun setOnReviewListener(listener: OnReviewClicked) {
        this.onReviewClicked = listener
    }
}