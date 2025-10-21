

// file: ui/movielist/MovieListActivity.kt
package com.example.appmoive.ui.movielist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivityMovieListBinding
import com.example.appmoive.ui.adapters.MovieListAdapter
import com.example.appmoive.ui.adapters.OnMovieClickListener // <-- SỬA 1: Import interface
import com.example.appmoive.ui.detail.DetailActivity

// SỬA 2: Implement interface OnMovieClickListener
class MovieListActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var binding: ActivityMovieListBinding
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchPopularMovies()
    }

    private fun setupRecyclerView() {
        // SỬA 3: Truyền "this" vào làm listener khi khởi tạo adapter
        movieListAdapter = MovieListAdapter(mutableListOf(), this) // Thêm "this"
        binding.rvMovieList.apply {
            layoutManager = LinearLayoutManager(this@MovieListActivity)
            adapter = movieListAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (viewModel.isLoading.value != true && totalItemCount <= lastVisibleItemPosition + 3) {
                        viewModel.fetchPopularMovies()
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this) { movies ->
            movies?.let {
                movieListAdapter.addMovies(it)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    // SỬA 4: Override lại hàm onMovieClick để xử lý sự kiện
    override fun onMovieClick(movie: Movie) {
        // Log để kiểm tra
        Log.d("MovieListActivity", "Clicked on movie: ${movie.title} (ID: ${movie.id})")

        // Tạo Intent để mở DetailActivity
        val intent = Intent(this, DetailActivity::class.java).apply {
            // Đặt ID của phim vào intent
            putExtra("MOVIE_ID", movie.id)
        }
        // Khởi chạy DetailActivity
        startActivity(intent)
    }
}