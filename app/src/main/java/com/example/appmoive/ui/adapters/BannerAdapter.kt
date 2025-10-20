// file: ui/adapters/BannerAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ItemMovieBannerBinding
import com.example.appmoive.utils.Constants

class BannerAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(val binding: ItemMovieBannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemMovieBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val movie = movies[position]

        // Chọn ảnh để hiển thị: ưu tiên backdrop, nếu không có thì dùng poster
        val imageUrl = if (!movie.backdropPath.isNullOrEmpty()) {
            movie.backdropPath
        } else {
            movie.posterPath
        }

        holder.binding.apply {
            Glide.with(root.context)
                .load(Constants.IMAGE_BASE_URL + imageUrl)
                .into(ivBannerImage)

            tvBannerTitle.text = movie.title
            tvBannerDescription.text = "Trending movie of the day"
        }
    }

    fun setData(newMovies: List<Movie>) {
        this.movies = newMovies
        notifyDataSetChanged()
    }
}