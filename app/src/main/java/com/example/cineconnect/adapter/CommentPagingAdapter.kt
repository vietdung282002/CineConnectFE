package com.example.cineconnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.databinding.CommentItemBinding
import com.example.cineconnect.model.Comment
import com.example.cineconnect.onClickInterface.OptionsMenuClickListener
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils

class CommentPagingAdapter :
    PagingDataAdapter<Comment, CommentPagingAdapter.CommentViewHolder>(CommentComparator) {

    private var listener: OptionsMenuClickListener? = null

    class CommentViewHolder(commentItemBinding: CommentItemBinding) :
        RecyclerView.ViewHolder(commentItemBinding.root) {
        val profileImage = commentItemBinding.profileImage
        val username = commentItemBinding.tvUsername
        val comment = commentItemBinding.content
        val btnMore = commentItemBinding.btnMore
//        val date = commentItemBinding.tvTime
    }

    object CommentComparator : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.user == newItem.user
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        if (comment != null) {
            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth * 0.08).toInt()

            val layoutParams = holder.profileImage.layoutParams

            layoutParams.width = itemWidth
            layoutParams.height = itemWidth

            holder.profileImage.layoutParams = layoutParams

            Glide.with(holder.itemView.context)
                .load(Utils.USER_PROFILE_LINK + comment.user.profilePic).into(holder.profileImage)
            val htmlText =
                "<font color='#FFFFFFFF'><b>${comment.user.username}</b></font>   <font color='#9F9A9A'>${
                    Utils.getRelativeTime(comment.timeStamp)
                }</font>"
            holder.username.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.comment.text = comment.comment
            if (comment.user.id == SessionManager.getUserId(holder.itemView.context)) {
                holder.btnMore.visibility = View.VISIBLE
                holder.btnMore.setOnClickListener {
                    val popupMenu = PopupMenu(holder.itemView.context, holder.btnMore)
                    popupMenu.inflate(R.menu.options_menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item?.itemId) {
                            R.id.delete -> {
                                listener?.onDeleteClicked(position, comment.id)
                            }

                            R.id.edit -> {
                                listener?.onEditClicked(position, comment.id)
                            }

                        }
                        false
                    }
                    popupMenu.show()
                }
            }
        }
    }

    fun setOptionsMenuClickListener(listener: OptionsMenuClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val adapterLayout =
            CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(adapterLayout)
    }

}