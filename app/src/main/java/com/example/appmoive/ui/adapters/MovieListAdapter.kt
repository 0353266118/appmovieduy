// file: ui/adapters/MovieListAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ItemTopSearchBinding
import com.example.appmoive.utils.Constants

// SỬA 1: Thêm listener vào constructor
class MovieListAdapter(
    private var movies: MutableList<Movie>,
    private val listener: OnMovieClickListener
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemTopSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemTopSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(Constants.IMAGE_BASE_URL + movie.posterPath)
                .into(ivPoster)
            tvMovieTitle.text = movie.title
            tvMovieGenre.text = "Action, Adventure"
        }

        // SỬA 2: Thêm sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener {
            listener.onMovieClick(movie)
        }
    }

    fun addMovies(newMovies: List<Movie>) {
        val oldSize = movies.size
        movies.addAll(newMovies)
        notifyItemRangeInserted(oldSize, newMovies.size)
    }
}