// file: ui/adapters/TopRatedAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ItemMoviePosterBinding
import com.example.appmoive.utils.Constants


class TopRatedAdapter(
    private var movies: List<Movie>,
    private val listener: OnMovieClickListener
) : RecyclerView.Adapter<TopRatedAdapter.TopRatedViewHolder>() {

    inner class TopRatedViewHolder(val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedViewHolder {
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopRatedViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: TopRatedViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(Constants.IMAGE_BASE_URL + movie.posterPath)
                .into(ivPoster)

            tvMovieTitle.text = movie.title
            tvMovieGenre.text = "Action, Adventure"
        }


        holder.itemView.setOnClickListener {
            listener.onMovieClick(movie)
        }
    }

    fun setData(newMovies: List<Movie>) {
        this.movies = newMovies
        notifyDataSetChanged()
    }
}