package com.example.cineconnect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.ActivityItemBinding
import com.example.cineconnect.model.Activity
import com.example.cineconnect.onClickInterface.OnActivityClicked
import com.example.cineconnect.utils.Utils

class ActivityPagingAdapter :
    PagingDataAdapter<Activity, ActivityPagingAdapter.ActivityViewHolder>(CommentComparator) {

    private var onActivityClicked: OnActivityClicked? = null

    class ActivityViewHolder(activityItemBinding: ActivityItemBinding) :
        RecyclerView.ViewHolder(activityItemBinding.root) {
        val profileImage = activityItemBinding.profileImage
        val activityText = activityItemBinding.tvActivity
        val timeStamp = activityItemBinding.timestamp
        val layout = activityItemBinding.layoutMain
    }

    object CommentComparator : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.user == newItem.user
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = getItem(position)
        if (activity != null) {
            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth * 0.08).toInt()

            val layoutParams = holder.profileImage.layoutParams

            layoutParams.width = itemWidth
            layoutParams.height = itemWidth

            holder.profileImage.layoutParams = layoutParams

            Glide.with(holder.itemView.context)
                .load(Utils.USER_PROFILE_LINK + activity.user.profilePic).into(holder.profileImage)
            holder.timeStamp.text = Utils.getRelativeTime(activity.timeStamp)
            var htmlText = ""
            when (activity.type) {
                1 -> {
                    htmlText =
                        "<font color='#9F9A9A'>You reviewed</font> <font color='#9F9A9A'><b>${(activity.review?.movie?.title)}</b></font>"
                    holder.layout.setOnClickListener {
                        activity.review?.id?.let { reviewId ->
                            onActivityClicked?.onReviewClicked(
                                position,
                                reviewId
                            )
                        }
                    }
                }

                2 -> {
                    htmlText =
                        "<font color='#9F9A9A'>You liked</font> <font color='#9F9A9A'><b>${(activity.movie?.title)}</b></font>"
                    holder.layout.setOnClickListener {
                        activity.movie?.id?.let { id ->
                            onActivityClicked?.onMovieClicked(
                                position,
                                id
                            )
                        }
                    }
                }

                3 -> {
                    htmlText =
                        "<font color='#9F9A9A'>You watched</font> <font color='#9F9A9A'><b>${(activity.movie?.title)}</b></font>"
                    holder.layout.setOnClickListener {
                        activity.movie?.id?.let { id ->
                            onActivityClicked?.onMovieClicked(
                                position,
                                id
                            )
                        }
                    }

                }

                4 -> {
                    htmlText =
                        "<font color='#9F9A9A'>You liked</font> <font color='#9F9A9A'><b>${activity.review?.user?.username}</b></font><font color='#9F9A9A'>'s review of</font> <font color='#9F9A9A'><b>${activity.review?.movie?.title}</b></font>"
                    holder.layout.setOnClickListener {
                        activity.review?.id?.let { reviewId ->
                            onActivityClicked?.onReviewClicked(
                                position,
                                reviewId
                            )
                        }
                    }
                }

                5 -> {
                    htmlText =
                        "<font color='#9F9A9A'>You rated</font> <font color='#9F9A9A'><b>${(activity.movie?.title)}</b></font>"
                    holder.layout.setOnClickListener {
                        activity.movie?.id?.let { id ->
                            onActivityClicked?.onMovieClicked(
                                position,
                                id
                            )
                        }
                    }
                }

                6 -> {
                    htmlText =
                        "<font color='#9F9A9A'>You followed</font> <font color='#9F9A9A'><b>${(activity.userFollow?.username)}</b></font>"
                    holder.layout.setOnClickListener {
                        activity.userFollow?.id?.let { id ->
                            onActivityClicked?.onUserClicked(
                                position,
                                id
                            )
                        }
                    }
                }

                else -> {
                    htmlText =
                        "<font color='#9F9A9A'>You comment on </font> <font color='#9F9A9A'><b>${activity.review?.user?.username}</b></font><font color='#9F9A9A'>'s review of</font> <font color='#9F9A9A'><b>${activity.review?.movie?.title}</b></font>"
                    holder.layout.setOnClickListener {
                        activity.review?.id?.let { reviewId ->
                            onActivityClicked?.onReviewClicked(
                                position,
                                reviewId
                            )
                        }
                    }
                }
            }
            holder.activityText.text =
                HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val adapterLayout =
            ActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(adapterLayout)
    }

    fun setOnActivityClicked(onActivityClicked: OnActivityClicked) {
        this.onActivityClicked = onActivityClicked
    }
}