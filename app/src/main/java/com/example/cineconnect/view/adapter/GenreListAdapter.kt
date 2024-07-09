package com.example.cineconnect.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cineconnect.databinding.GenreItemBinding
import com.example.cineconnect.model.entities.Genre
import com.example.cineconnect.view.onClickInterface.OnGenreClicked

class GenreListAdapter : ListAdapter<Genre, GenreListAdapter.GenreViewHolder>(GenreItemDiffUtil()) {
    private var listener: OnGenreClicked? = null

    class GenreItemDiffUtil : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }

    }

    class GenreViewHolder(genreItemBinding: GenreItemBinding) :
        RecyclerView.ViewHolder(genreItemBinding.root) {
        val genreName = genreItemBinding.genreName
        val genreDetail = genreItemBinding.genreDetail
    }

    override fun submitList(list: List<Genre>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val adapterLayout =
            GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = getItem(position)
        holder.genreName.text = genre.name
        holder.genreDetail.setOnClickListener {
            listener?.getOnGenreClicked(position, genre.id, genre.name)
        }
    }

    fun setOnGenreClicked(listener: OnGenreClicked) {
        this.listener = listener
    }
}