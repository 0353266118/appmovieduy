package com.example.appmoive.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmoive.R
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivitySearchBinding
import com.example.appmoive.ui.adapters.MovieListAdapter
import com.example.appmoive.ui.adapters.OnMovieClickListener
import com.example.appmoive.ui.detail.DetailActivity
import kotlinx.coroutines.*
class SearchActivity : AppCompatActivity(), OnMovieClickListener {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: MovieListAdapter // Tái sử dụng MovieListAdapter

    private var searchJob: Job? = null // Biến để quản lý coroutine debounce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Lắng nghe sự kiện thay đổi text trong ô tìm kiếm
        binding.etSearch.addTextChangedListener { editable ->
            val query = editable.toString().trim()

            // Hủy bỏ job tìm kiếm cũ
            searchJob?.cancel()
            // Tạo một job mới với độ trễ (debounce)
            searchJob = MainScope().launch {
                delay(500L) // Chờ 500ms sau khi người dùng ngừng gõ
                viewModel.searchMovies(query)
            }
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = MovieListAdapter(mutableListOf(), this)
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(this) { movies ->
            // MovieListAdapter cần hàm setData, chúng ta sẽ thêm nó
            searchAdapter.setData(movies ?: emptyList())
        }
    }

    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }
}