package com.example.cineconnect.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.CastItemBinding
import com.example.cineconnect.model.entities.CastList
import com.example.cineconnect.utils.Utils.Companion.PROFILE_LINK
import com.example.cineconnect.view.onClickInterface.OnPersonClicked

class CastListAdapter : ListAdapter<CastList, CastListAdapter.CastViewHolder>(CastItemDiffUtils()) {
    private var listener: OnPersonClicked? = null

    class CastItemDiffUtils : DiffUtil.ItemCallback<CastList>() {
        override fun areItemsTheSame(oldItem: CastList, newItem: CastList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CastList, newItem: CastList): Boolean {
            return oldItem == newItem
        }

    }

    override fun submitList(list: List<CastList>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class CastViewHolder(castItemBinding: CastItemBinding) :
        RecyclerView.ViewHolder(castItemBinding.root) {
        val castPoster = castItemBinding.castPoster
        val castName = castItemBinding.castName
        val castRole = castItemBinding.castRole
        val castDetailBtn = castItemBinding.castDetailBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val adapterLayout =
            CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = getItem(position)
        val castPoster = PROFILE_LINK + cast.profilePath

        val displayMetrics = holder.itemView.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.13).toInt()

        val layoutParams = holder.castPoster.layoutParams

        layoutParams.width = itemWidth
        layoutParams.height = itemWidth
        holder.castPoster.layoutParams = layoutParams

        Glide.with(holder.itemView.context).load(castPoster).into(holder.castPoster)

        holder.castName.text = cast.name
        holder.castRole.text = cast.character
        holder.castDetailBtn.setOnClickListener {
            listener?.getOnPersonClicked(position, cast.id)
        }
    }

    fun setOnPersonClicked(listener: OnPersonClicked) {
        this.listener = listener
    }
}