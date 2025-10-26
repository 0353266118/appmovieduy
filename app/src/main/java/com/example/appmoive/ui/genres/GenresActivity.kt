// file: ui/genres/GenresActivity.kt
package com.example.appmoive.ui.genres

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoive.R // Import R để truy cập dimens
import com.example.appmoive.data.model.Genre
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivityGenresBinding
import com.example.appmoive.ui.adapters.GenreChipAdapter
import com.example.appmoive.ui.adapters.MovieAdapter
import com.example.appmoive.ui.adapters.OnGenreClickListener
import com.example.appmoive.ui.adapters.OnMovieClickListener
import com.example.appmoive.ui.detail.DetailActivity
import com.example.appmoive.utils.GridSpacingItemDecoration

class GenresActivity : AppCompatActivity(), OnMovieClickListener, OnGenreClickListener {

    private lateinit var binding: ActivityGenresBinding
    private val viewModel: GenresViewModel by viewModels()
    private lateinit var genreChipAdapter: GenreChipAdapter
    private lateinit var moviesAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()

        viewModel.fetchGenres()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Setup RecyclerView cho chip thể loại (ngang)
        genreChipAdapter = GenreChipAdapter(emptyList(), this)
        binding.rvGenreChips.apply {
            layoutManager = LinearLayoutManager(this@GenresActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = genreChipAdapter
        }

        // Setup RecyclerView cho lưới phim (2 cột)
        moviesAdapter = MovieAdapter(emptyList(), this)
        binding.rvGenreMovies.apply {
            val gridLayoutManager = GridLayoutManager(this@GenresActivity, 2)
            layoutManager = gridLayoutManager
            adapter = moviesAdapter

            // Thêm ItemDecoration để căn chỉnh lưới cho đẹp
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
            addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))

            // Thêm listener để xử lý cuộn vô hạn
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = gridLayoutManager.itemCount
                    val lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()

                    // Lấy genreId hiện tại từ ViewModel để biết cần tải thêm cho thể loại nào
                    val genreId = viewModel.currentGenreId

                    // Nếu đã cuộn gần đến cuối và có genreId hợp lệ
                    if (genreId != null && totalItemCount <= lastVisibleItemPosition + 4) {
                        viewModel.fetchMoviesByGenre(genreId)
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        // Lắng nghe danh sách thể loại
        viewModel.genres.observe(this) { genres ->
            genres?.let {
                genreChipAdapter.setData(it)
                // Tự động tải phim cho thể loại đầu tiên khi có dữ liệu
                if (it.isNotEmpty()) {
                    onGenreClick(it.first())
                }
            }
        }

        // Lắng nghe danh sách phim theo thể loại
        viewModel.moviesByGenre.observe(this) { movies ->
            movies?.let {
                // Luôn cập nhật toàn bộ danh sách, vì ViewModel đã quản lý việc cộng dồn
                moviesAdapter.setData(it)
            }
        }
    }

    // Xử lý sự kiện khi click vào một chip thể loại
    override fun onGenreClick(genre: Genre) {
        // Ra lệnh cho ViewModel tải phim cho thể loại mới này
        viewModel.fetchMoviesByGenre(genre.id)
        // Cuộn danh sách phim về đầu trang
        binding.rvGenreMovies.scrollToPosition(0)
    }

    // Xử lý sự kiện khi click vào một phim trong lưới
    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }
}