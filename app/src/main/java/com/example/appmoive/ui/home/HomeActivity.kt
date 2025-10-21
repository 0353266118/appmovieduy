// file: ui/home/HomeActivity.kt
package com.example.appmoive.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivityHomeBinding
import com.example.appmoive.ui.adapters.* // <-- Import tất cả adapter
import com.example.appmoive.ui.detail.DetailActivity // <-- Import DetailActivity
import com.example.appmoive.ui.movielist.MovieListActivity

// SỬA 1: Implement interface OnMovieClickListener
class HomeActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var recommendedAdapter: MovieAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var topRatedAdapter: TopRatedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup các RecyclerView và ViewPager2
        setupRecyclerView()
        setupBannerViewPager()
        setupTopRatedRecyclerView()

        observeViewModel()

        // Lấy dữ liệu
        homeViewModel.fetchPopularMovies()
        homeViewModel.fetchTrendingMovies()
        homeViewModel.fetchTopRatedMovies()

        // Sự kiện click cho See All
        binding.tvSeeAllRecommended.setOnClickListener {
            val intent = Intent(this, MovieListActivity::class.java)
            startActivity(intent)
        }
    }

    // SỬA 2: Truyền "this" vào làm listener khi khởi tạo adapter
    private fun setupRecyclerView() {
        recommendedAdapter = MovieAdapter(emptyList(), this)
        binding.rvRecommended.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter

            // THÊM CÁC DÒNG NÀY VÀO
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }

    private fun setupTopRatedRecyclerView() {
        topRatedAdapter = TopRatedAdapter(emptyList(), this) // Thêm "this"
        binding.rvTopRated.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedAdapter

            // THÊM CÁC DÒNG NÀY VÀO
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }

    private fun setupBannerViewPager() {
        bannerAdapter = BannerAdapter(emptyList(), this) // Thêm "this"
        binding.viewPagerBanner.adapter = bannerAdapter
    }

    private fun observeViewModel() {
        homeViewModel.popularMovies.observe(this) { movies ->
            movies?.let { recommendedAdapter.setData(it) }
        }

        homeViewModel.trendingMovies.observe(this) { movies ->
            movies?.let { bannerAdapter.setData(it.take(5)) }
        }

        homeViewModel.topRatedMovies.observe(this) { movies ->
            movies?.let { topRatedAdapter.setData(it.take(10)) }
        }
    }

    // SỬA 3: Override lại hàm onMovieClick để xử lý sự kiện
    override fun onMovieClick(movie: Movie) {
        // Log để kiểm tra xem click có hoạt động không
        Log.d("HomeActivity", "Clicked on movie: ${movie.title} (ID: ${movie.id})")

        // Tạo Intent để mở DetailActivity
        val intent = Intent(this, DetailActivity::class.java).apply {
            // Đặt ID của phim vào intent
            putExtra("MOVIE_ID", movie.id)
        }
        // Khởi chạy DetailActivity
        startActivity(intent)
    }
}