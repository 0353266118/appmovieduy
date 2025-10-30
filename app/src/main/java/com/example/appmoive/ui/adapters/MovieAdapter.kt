// file: ui/adapters/MovieAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ItemMoviePosterBinding
import com.example.appmoive.utils.Constants.IMAGE_BASE_URL


class MovieAdapter(
    private var movies: List<Movie>,
    private val listener: OnMovieClickListener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(IMAGE_BASE_URL + movie.posterPath)
                .into(ivPoster)

            tvMovieTitle.text = movie.title
            tvMovieGenre.text = "Action, Adventure"
        }

        // Thêm sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener {
            listener.onMovieClick(movie)
        }
    }

    fun setData(newMovies: List<Movie>) {
        this.movies = newMovies
        notifyDataSetChanged()
    }
}