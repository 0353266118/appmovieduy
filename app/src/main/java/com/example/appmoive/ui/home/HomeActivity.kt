// file: ui/home/HomeActivity.kt
package com.example.appmoive.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoive.R
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivityHomeBinding
import com.example.appmoive.ui.adapters.*
import com.example.appmoive.ui.detail.DetailActivity
import com.example.appmoive.ui.favorites.FavoritesActivity
import com.example.appmoive.ui.movielist.MovieListActivity
import com.example.appmoive.ui.search.SearchActivity
import com.example.appmoive.ui.settings.SettingsActivity
import com.bumptech.glide.Glide
import com.example.appmoive.ui.genres.GenresActivity
import com.google.firebase.auth.FirebaseAuth

// Đảm bảo class implement OnMovieClickListener
class HomeActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var firebaseAuth: FirebaseAuth // <<-- THÊM BIẾN NÀY
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var recommendedAdapter: MovieAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var topRatedAdapter: TopRatedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Tải thông tin người dùng
        loadUserProfile()

        // Bắt đầu chạy hiệu ứng Shimmer ngay khi activity được tạo
        binding.shimmerRecommended.startShimmer()
        // (Thêm dòng này nếu em đã làm shimmer cho Top Rated)
        // binding.shimmerTopRated.startShimmer()

        setupRecyclerView()
        setupBannerViewPager()
        setupTopRatedRecyclerView()
        // BẮT ĐẦU PHẦN CODE MỚI CHO BOTTOM NAVIGATION
        // ================================================================
        setupBottomNavigation()

        observeViewModel()

        homeViewModel.fetchPopularMovies()
        homeViewModel.fetchTrendingMovies()
        homeViewModel.fetchTopRatedMovies()

        binding.tvSeeAllRecommended.setOnClickListener {
            val intent = Intent(this, MovieListActivity::class.java).apply {
                putExtra("LIST_TYPE", "popular")
                putExtra("LIST_TITLE", "Recommended For You")
            }
            startActivity(intent)
        }

        // MỚI: Click vào See All của Top Rated
        binding.tvSeeAllTopRated.setOnClickListener {
            val intent = Intent(this, MovieListActivity::class.java).apply {
                putExtra("LIST_TYPE", "top_rated")
                putExtra("LIST_TITLE", "Top Rated Movies")
            }
            startActivity(intent)
        }

        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Mỗi khi quay lại màn hình Home, tải lại thông tin user để cập nhật tên/ảnh mới
        loadUserProfile()
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }

    private fun loadUserProfile() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            // Có người dùng đăng nhập
            val username = user.displayName
            if (!username.isNullOrEmpty()) {
                binding.tvGreetingName.text = "Hi, $username"
            } else {
                // Nếu user chưa đặt tên, hiển thị email
                binding.tvGreetingName.text = "Hi, ${user.email}"
            }

            // Tải ảnh đại diện
            if (user.photoUrl != null) {
                Glide.with(this).load(user.photoUrl).into(binding.ivAvatar)
            } else {
                // Nếu không có ảnh, dùng ảnh mặc định
                binding.ivAvatar.setImageResource(R.drawable.placeholder_avatar) // Dùng cùng ảnh mặc định
            }
        } else {
            // Trường hợp không có ai đăng nhập (lỗi hoặc đã đăng xuất)
            // Có thể chuyển về màn hình Login ở đây nếu cần
            binding.tvGreetingName.text = "Hi, Guest"
        }
    }

    // MỚI: Hàm để thiết lập BottomNavigationView
    private fun setupBottomNavigation() {
        // Đảm bảo item "Home" được chọn khi khởi động
        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Hiện tại đang ở màn hình Home, nên không cần làm gì
                    // Hoặc có thể cuộn lên đầu trang
                    binding.nestedScrollView.smoothScrollTo(0, 0)
                    true // Trả về true để báo hiệu sự kiện đã được xử lý
                }
                R.id.nav_search -> {
                    // SỬA: Mở GenresActivity thay vì SearchActivity
                    val intent = Intent(this, GenresActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_favorites -> {
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // MỞ MÀN HÌNH SETTINGS
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
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
    // MỚI: Xử lý khi quay lại HomeActivity từ màn hình khác
//    override fun onResume() {
//        super.onResume()
//        // Đảm bảo item "Home" luôn được chọn khi người dùng quay lại màn hình này
//        binding.bottomNavigation.selectedItemId = R.id.nav_home
//    }
}