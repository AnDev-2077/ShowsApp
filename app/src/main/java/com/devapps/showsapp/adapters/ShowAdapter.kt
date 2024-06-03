package com.devapps.showsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devapps.showsapp.R
import com.devapps.showsapp.TvShowItem
import com.devapps.showsapp.databinding.ItemShowBinding
import com.squareup.picasso.Picasso

class ShowAdapter (
    var responseShows: List<TvShowItem>,
    var favoriteShows: List<TvShowItem>,
    private val onShowClick:(TvShowItem) -> Unit,
    private val onStarClick:(TvShowItem) -> Unit): RecyclerView.Adapter<ShowAdapter.ShowViewHolder>() {

    class ShowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemShowBinding.bind(view)

        fun bind(responseShows: TvShowItem, favoriteShows: List<TvShowItem>, onShowClick: (TvShowItem) -> Unit, onStarClick: (TvShowItem) -> Unit){
            binding.tvShowName.text = responseShows.name
            binding.tvShowNetwork.text = responseShows.network
            Picasso.get().load(responseShows.image_thumbnail_path).into(binding.ivShow)
            var isFavorite = favoriteShows.any { it.id == responseShows.id }
            binding.ivStar.setImageResource(if (isFavorite) R.drawable.ic_star_solid else R.drawable.ic_star_regular)
            binding.root.setOnClickListener {
                onShowClick(responseShows)
            }

            binding.ivStar.setOnClickListener {
                onStarClick(responseShows)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ShowViewHolder(layoutInflater.inflate(R.layout.item_show, parent, false))
    }

    override fun getItemCount(): Int {
        return responseShows.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val item = responseShows[position]
        holder.bind(item, favoriteShows, onShowClick, onStarClick)
    }
}