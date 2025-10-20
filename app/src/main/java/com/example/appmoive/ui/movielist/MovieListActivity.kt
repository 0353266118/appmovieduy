// file: ui/movielist/MovieListActivity.kt
package com.example.appmoive.ui.movielist

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoive.databinding.ActivityMovieListBinding
import com.example.appmoive.ui.adapters.MovieListAdapter // <-- Import adapter từ đúng vị trí

class MovieListActivity : AppCompatActivity() {

    // 1. Khai báo các biến cần thiết
    private lateinit var binding: ActivityMovieListBinding
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieListAdapter: MovieListAdapter // <-- Đổi tên biến cho rõ ràng

    override fun onCreate(savedInstanceState: Bundle?) {
        // 2. Gọi super.onCreate() là bắt buộc
        super.onCreate(savedInstanceState)
        // 3. Khởi tạo ViewBinding
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener {
            // Hàm này sẽ mô phỏng việc người dùng nhấn nút back vật lý
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        observeViewModel()

        // Tải trang đầu tiên
        viewModel.fetchPopularMovies()
    }

    private fun setupRecyclerView() {
        // 4. Khởi tạo adapter với danh sách có thể thay đổi (mutableListOf)
        movieListAdapter = MovieListAdapter(mutableListOf())
        binding.rvMovieList.apply {
            layoutManager = LinearLayoutManager(this@MovieListActivity)
            adapter = movieListAdapter

            // 5. Thêm listener để xử lý cuộn vô hạn
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    // Nếu không đang tải và người dùng đã cuộn gần đến cuối
                    if (viewModel.isLoading.value != true && totalItemCount <= lastVisibleItemPosition + 3) {
                        viewModel.fetchPopularMovies()
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        // Lắng nghe danh sách phim mới
        viewModel.movies.observe(this) { movies ->
            movies?.let {
                movieListAdapter.addMovies(it)
            }
        }

        // Lắng nghe trạng thái tải để hiện/ẩn ProgressBar
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}