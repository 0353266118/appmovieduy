// file: ui/adapters/FavoritesAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.FavoriteMovie
import com.example.appmoive.databinding.ItemTopSearchBinding
import com.example.appmoive.utils.Constants


class FavoritesAdapter(
    private var movies: List<FavoriteMovie>,
    private val listener: OnFavoriteClickListener
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: ItemTopSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemTopSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(Constants.IMAGE_BASE_URL + movie.posterPath)
                .into(ivPoster)
            tvMovieTitle.text = movie.title
            tvMovieGenre.text = movie.overview

            // Thêm sự kiện click
            // Click vào cả item
            holder.itemView.setOnClickListener {
                listener.onFavoriteMovieClick(movie)
            }

            // Click vào icon trái tim
            ivItemFavorite.setOnClickListener {
                listener.onRemoveFavoriteClick(movie)
            }
        }
    }

    fun setData(newMovies: List<FavoriteMovie>) {
        this.movies = newMovies
        notifyDataSetChanged()
    }
}