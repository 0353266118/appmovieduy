// file: ui/home/HomeActivity.kt
package com.example.appmoive.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoive.databinding.ActivityHomeBinding
import com.example.appmoive.ui.adapters.BannerAdapter
import com.example.appmoive.ui.adapters.MovieAdapter
import com.example.appmoive.ui.adapters.TopRatedAdapter // <-- THAY ĐỔI 1: Import adapter mới
import com.example.appmoive.ui.movielist.MovieListActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var recommendedAdapter: MovieAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var topRatedAdapter: TopRatedAdapter // <-- THAY ĐỔI 2: Khai báo adapter mới

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup các RecyclerView và ViewPager2
        setupRecyclerView()
        setupBannerViewPager()
        setupTopRatedRecyclerView() // Gọi hàm setup cho Top Rated

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

    private fun setupRecyclerView() {
        recommendedAdapter = MovieAdapter(emptyList())
        binding.rvRecommended.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }
    }

    // THAY ĐỔI 3: Hàm setup cho Top Rated bây giờ sẽ dùng TopRatedAdapter
    private fun setupTopRatedRecyclerView() {
        topRatedAdapter = TopRatedAdapter(emptyList()) // Khởi tạo adapter mới
        binding.rvTopRated.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedAdapter // Gán adapter mới
        }
    }

    private fun setupBannerViewPager() {
        bannerAdapter = BannerAdapter(emptyList())
        binding.viewPagerBanner.adapter = bannerAdapter
    }

    private fun observeViewModel() {
        homeViewModel.popularMovies.observe(this) { movies ->
            movies?.let {
                recommendedAdapter.setData(it)
            }
        }

        homeViewModel.trendingMovies.observe(this) { movies ->
            movies?.let {
                bannerAdapter.setData(it.take(5))
            }
        }

        // THAY ĐỔI 4: Observer bây giờ sẽ cập nhật cho topRatedAdapter
        homeViewModel.topRatedMovies.observe(this) { movies ->
            movies?.let {
                Log.d("HomeActivity", "Top Rated movies loaded: ${it.size} items")
                topRatedAdapter.setData(it.take(10)) // Cập nhật cho adapter mới
            }
        }
    }
}