// file: ui/favorites/FavoritesActivity.kt
package com.example.appmoive.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoive.data.model.FavoriteMovie
import com.example.appmoive.databinding.ActivityFavoritesBinding
import com.example.appmoive.ui.adapters.FavoritesAdapter
import com.example.appmoive.ui.adapters.OnFavoriteClickListener // Import interface
import com.example.appmoive.ui.detail.DetailActivity

// SỬA 1: Implement interface
class FavoritesActivity : AppCompatActivity(), OnFavoriteClickListener {

    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        // SỬA 2: Truyền listener vào adapter
        favoritesAdapter = FavoritesAdapter(emptyList(), this)
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = favoritesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.allFavoriteMovies.observe(this) { movies ->
            if (movies.isNullOrEmpty()) {
                binding.rvFavorites.visibility = View.GONE
                binding.layoutEmptyFavorites.visibility = View.VISIBLE
            } else {
                binding.rvFavorites.visibility = View.VISIBLE
                binding.layoutEmptyFavorites.visibility = View.GONE
                favoritesAdapter.setData(movies)
            }
        }
    }

    // SỬA 3: Implement các hàm của interface
    override fun onFavoriteMovieClick(movie: FavoriteMovie) {
        // Mở DetailActivity, truyền ID phim
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }

    override fun onRemoveFavoriteClick(movie: FavoriteMovie) {
        // Gọi ViewModel để xóa phim
        viewModel.removeFromFavorites(movie)
    }
}