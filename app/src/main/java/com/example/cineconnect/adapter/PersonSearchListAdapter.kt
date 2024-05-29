package com.example.cineconnect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cineconnect.databinding.MoviePersonItemSearchBinding
import com.example.cineconnect.model.CastList
import com.example.cineconnect.model.Person
import com.example.cineconnect.onClickInterface.OnPersonClicked
import com.example.cineconnect.utils.Utils

class PersonSearchListAdapter:ListAdapter<Person,PersonSearchListAdapter.PersonViewHolder>(PersonDiffUtil()){
    private var listener: OnPersonClicked? = null

    class PersonDiffUtil: DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }

    }
    override fun submitList(list: List<Person>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class PersonViewHolder(binding: MoviePersonItemSearchBinding): RecyclerView.ViewHolder(binding.root) {
        val personPoster = binding.posterImage
        val content = binding.tvContent
        val layout = binding.flItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val adapterLayout =
            MoviePersonItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        val displayMetrics = holder.itemView.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth * 0.2).toInt()
        val itemHeight = (itemWidth * (1.5)).toInt()
        val layoutParams = holder.personPoster.layoutParams

        layoutParams.width = itemWidth
        layoutParams.height = itemHeight
        holder.personPoster.layoutParams = layoutParams

        Glide.with(holder.itemView.context).load(Utils.PROFILE_LINK + person.profilePath)
            .into(holder.personPoster)
        val htmlText =
            "<font color='#FFFFFFFF'><b>${person.name} </b></font>"
        holder.content.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.layout.setOnClickListener{
            listener?.getOnPersonClicked(position,person.id)
        }
    }

    fun setOnPersonClicked(listener: OnPersonClicked) {
        this.listener = listener
    }

}