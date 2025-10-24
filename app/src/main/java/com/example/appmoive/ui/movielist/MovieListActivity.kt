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
import com.example.appmoive.ui.adapters.OnMovieClickListener
import com.example.appmoive.ui.detail.DetailActivity

class MovieListActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var binding: ActivityMovieListBinding
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Nhận dữ liệu từ Intent
        val listType = intent.getStringExtra("LIST_TYPE") ?: "popular"
        val listTitle = intent.getStringExtra("LIST_TITLE") ?: "Movies"

        // 2. Gán dữ liệu nhận được cho ViewModel và UI
        viewModel.listType = listType
        binding.tvHeaderTitle.text = listTitle

        // 3. Setup các thành phần còn lại
        setupUI()
        setupRecyclerView()
        observeViewModel()

        // 4. Gọi hàm tải phim "đa năng" mới
        viewModel.fetchMovies()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        movieListAdapter = MovieListAdapter(mutableListOf(), this)
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
                        // 5. Gọi hàm tải phim "đa năng" mới khi cuộn
                        viewModel.fetchMovies()
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

    override fun onMovieClick(movie: Movie) {
        Log.d("MovieListActivity", "Clicked on movie: ${movie.title} (ID: ${movie.id})")
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }
}