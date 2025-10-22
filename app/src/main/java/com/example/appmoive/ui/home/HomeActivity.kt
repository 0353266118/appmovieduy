// file: ui/home/HomeActivity.kt
package com.example.appmoive.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivityHomeBinding
import com.example.appmoive.ui.adapters.*
import com.example.appmoive.ui.detail.DetailActivity
import com.example.appmoive.ui.movielist.MovieListActivity
import com.example.appmoive.ui.search.SearchActivity

// Đảm bảo class implement OnMovieClickListener
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

        // Bắt đầu chạy hiệu ứng Shimmer ngay khi activity được tạo
        binding.shimmerRecommended.startShimmer()
        // (Thêm dòng này nếu em đã làm shimmer cho Top Rated)
        // binding.shimmerTopRated.startShimmer()

        setupRecyclerView()
        setupBannerViewPager()
        setupTopRatedRecyclerView()

        observeViewModel()

        homeViewModel.fetchPopularMovies()
        homeViewModel.fetchTrendingMovies()
        homeViewModel.fetchTopRatedMovies()

        binding.tvSeeAllRecommended.setOnClickListener {
            val intent = Intent(this, MovieListActivity::class.java)
            startActivity(intent)
        }

        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        recommendedAdapter = MovieAdapter(emptyList(), this)
        binding.rvRecommended.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }

    private fun setupTopRatedRecyclerView() {
        topRatedAdapter = TopRatedAdapter(emptyList(), this)
        binding.rvTopRated.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }

    private fun setupBannerViewPager() {
        bannerAdapter = BannerAdapter(emptyList(), this)
        binding.viewPagerBanner.adapter = bannerAdapter
    }

    private fun observeViewModel() {
        homeViewModel.popularMovies.observe(this) { movies ->
            movies?.let {
                // Tắt shimmer và hiện RecyclerView
                binding.shimmerRecommended.stopShimmer()
                binding.shimmerRecommended.visibility = View.GONE
                binding.rvRecommended.visibility = View.VISIBLE

                recommendedAdapter.setData(it)
            }
        }

        homeViewModel.trendingMovies.observe(this) { movies ->
            movies?.let {
                // (Thêm logic shimmer cho banner nếu cần)
                bannerAdapter.setData(it.take(5))
            }
        }

        homeViewModel.topRatedMovies.observe(this) { movies ->
            movies?.let {
                // (Thêm logic shimmer cho Top Rated nếu cần)
                topRatedAdapter.setData(it.take(10))
            }
        }
    }

    // ================================================================
    // HÀM onMovieClick NẰM Ở ĐÂY - NGANG HÀNG VỚI CÁC HÀM KHÁC
    // ================================================================
    override fun onMovieClick(movie: Movie) {
        Log.d("HomeActivity", "Clicked on movie: ${movie.title} (ID: ${movie.id})")
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }
}