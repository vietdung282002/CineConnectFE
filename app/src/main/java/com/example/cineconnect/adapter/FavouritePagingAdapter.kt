package com.example.cineconnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.UserFavouriteBinding
import com.example.cineconnect.model.FavouriteList
import com.example.cineconnect.utils.Utils.Companion.PROFILE_LINK

class FavouritePagingAdapter :
    PagingDataAdapter<FavouriteList, FavouritePagingAdapter.FavouriteViewHolder>(FavouriteComparator) {
    class FavouriteViewHolder(userFavouriteBinding: UserFavouriteBinding) :
        RecyclerView.ViewHolder(userFavouriteBinding.root) {
        val profileImage = userFavouriteBinding.profileImage
        val userName = userFavouriteBinding.tvUsername
        val userRating = userFavouriteBinding.ratingBar
        val layout = userFavouriteBinding.flItem
    }

    object FavouriteComparator : DiffUtil.ItemCallback<FavouriteList>() {
        override fun areItemsTheSame(oldItem: FavouriteList, newItem: FavouriteList): Boolean {
            return oldItem.user == newItem.user
        }

        override fun areContentsTheSame(oldItem: FavouriteList, newItem: FavouriteList): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val favourite = getItem(position)
        if (favourite != null) {
            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth * 0.1).toInt()

            val layoutParams = holder.profileImage.layoutParams

            layoutParams.width = itemWidth
            layoutParams.height = itemWidth

            holder.profileImage.layoutParams = layoutParams

            holder.userName.text = favourite.user.username
            if (favourite.rate != null) {
                holder.userRating.rating = favourite.rate.toFloat()
                holder.userRating.visibility = View.VISIBLE
            }
            Glide.with(holder.itemView).load(PROFILE_LINK + favourite.user.profilePic)
                .into(holder.profileImage)
            holder.layout.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val adapterLayout =
            UserFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(adapterLayout)
    }
}