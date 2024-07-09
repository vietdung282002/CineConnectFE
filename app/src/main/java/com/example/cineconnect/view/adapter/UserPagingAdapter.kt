package com.example.cineconnect.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.databinding.UserItemBinding
import com.example.cineconnect.model.entities.UserList
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.view.onClickInterface.OnFollowButtonClicked
import com.example.cineconnect.view.onClickInterface.OnUserClicked

class UserPagingAdapter : PagingDataAdapter<UserList, UserPagingAdapter.UserViewHolder>(
    UserComparator
) {
    private var onUserClicked: OnUserClicked? = null
    private var onFollowButtonClicked: OnFollowButtonClicked? = null

    object UserComparator : DiffUtil.ItemCallback<UserList>() {
        override fun areItemsTheSame(oldItem: UserList, newItem: UserList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserList, newItem: UserList): Boolean {
            return oldItem == newItem
        }
    }

    class UserViewHolder(userItemBinding: UserItemBinding): RecyclerView.ViewHolder(userItemBinding.root) {
        val profilePic = userItemBinding.profilePic
        val layout = userItemBinding.llItem
        val username = userItemBinding.tvUsername
        val followBtn = userItemBinding.followBtn
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        if(user != null) {
            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth * 0.1).toInt()
            val layoutParams = holder.profilePic.layoutParams

            layoutParams.width = itemWidth
            layoutParams.height = itemWidth
            holder.profilePic.layoutParams = layoutParams

            Glide.with(holder.itemView.context).load(Utils.USER_PROFILE_LINK + user.profilePic)
                .into(holder.profilePic)
            holder.username.text = user.username
            if(user.isFollowing == null){
                holder.followBtn.visibility = View.GONE
            }else{
                if(user.isFollowing!!){
                    holder.followBtn.text = "Following"
                    holder.followBtn.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.following_btn)
                    holder.followBtn.setTextColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.green
                        )
                    )

                }else{
                    holder.followBtn.text = "Follow"
                    holder.followBtn.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.follow_btn)
                    holder.followBtn.setTextColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.white
                        )
                    )

                }
            }
            holder.layout.setOnClickListener {
                onUserClicked?.getOnUserClicked(position, user.id)
            }
            holder.followBtn.setOnClickListener {
                onFollowButtonClicked?.getOnFollowButtonClicked(position, user.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val adapterLayout =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(adapterLayout)
    }

    fun setOnUserListener(onUserClicked: OnUserClicked) {
        this.onUserClicked = onUserClicked
    }

    fun setOnFollowButtonListener(onFollowButtonClicked: OnFollowButtonClicked) {
        this.onFollowButtonClicked = onFollowButtonClicked
    }
}