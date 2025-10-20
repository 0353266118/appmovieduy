// file: ui/home/HomeActivity.kt
package com.example.appmoive.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoive.databinding.ActivityHomeBinding
import com.example.appmoive.ui.adapters.BannerAdapter // <-- Đảm bảo đã import
import com.example.appmoive.ui.adapters.MovieAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var recommendedAdapter: MovieAdapter
    // KHAI BÁO BIẾN Ở ĐÂY ĐỂ TOÀN BỘ CLASS CÓ THỂ TRUY CẬP
    private lateinit var bannerAdapter: BannerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBannerViewPager() // Bây giờ hàm này sẽ không báo lỗi nữa

        observeViewModel()

        homeViewModel.fetchPopularMovies()
        homeViewModel.fetchTrendingMovies()
    }

    private fun setupRecyclerView() {
        recommendedAdapter = MovieAdapter(emptyList())
        binding.rvRecommended.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }
    }

    // Hàm này sẽ khởi tạo và gán adapter cho ViewPager2
    private fun setupBannerViewPager() {
        bannerAdapter = BannerAdapter(emptyList()) // Khởi tạo adapter
        binding.viewPagerBanner.adapter = bannerAdapter // Gán adapter
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
    }
}