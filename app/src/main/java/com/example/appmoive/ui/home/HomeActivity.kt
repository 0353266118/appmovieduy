// file: ui/home/HomeActivity.kt
package com.example.appmoive.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import com.example.appmoive.ui.auth.LoginActivity
import com.example.appmoive.ui.genres.GenresActivity
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var firebaseAuth: FirebaseAuth
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

        setupRecyclerView()
        setupBannerViewPager()
        setupTopRatedRecyclerView()

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

        //  Click vào See All của Top Rated
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
            val username = user.displayName
            if (!username.isNullOrEmpty()) {
                binding.tvGreetingName.text = "Hi, $username"
            } else {
                binding.tvGreetingName.text = "Hi, ${user.email}"
            }
            if (user.photoUrl != null) {
                Glide.with(this).load(user.photoUrl).into(binding.ivAvatar)
            } else {
                binding.ivAvatar.setImageResource(R.drawable.placeholder_avatar)
            }
        } else {
            //Nếu chưa đăng nhập, hiển thị giao diện khách
            binding.tvGreetingName.text = "Hello"
            binding.ivAvatar.setImageResource(R.drawable.placeholder_avatar)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            // SỬA LẠI LOGIC KHI CLICK
            // Kiểm tra đăng nhập cho các tab cần thiết
            val isLoggedIn = firebaseAuth.currentUser != null

            when (item.itemId) {
                R.id.nav_home, R.id.nav_search -> {
                    // Home và Search luôn có thể truy cập
                    if (item.itemId == R.id.nav_search) {
                        startActivity(Intent(this, GenresActivity::class.java))
                    }
                    true
                }
                R.id.nav_favorites, R.id.nav_profile -> {
                    if (isLoggedIn) {
                        // Nếu đã đăng nhập, mở màn hình tương ứng
                        if (item.itemId == R.id.nav_favorites) {
                            startActivity(Intent(this, FavoritesActivity::class.java))
                        } else {
                            startActivity(Intent(this, SettingsActivity::class.java))
                        }
                    } else {
                        // Nếu chưa, hiển thị hộp thoại yêu cầu đăng nhập
                        showLoginPromptDialog()
                    }
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
                binding.shimmerRecommended.stopShimmer()
                binding.shimmerRecommended.visibility = View.GONE
                binding.rvRecommended.visibility = View.VISIBLE

                recommendedAdapter.setData(it)
            }
        }

        homeViewModel.trendingMovies.observe(this) { movies ->
            movies?.let {
                bannerAdapter.setData(it.take(5))
            }
        }

        homeViewModel.topRatedMovies.observe(this) { movies ->
            movies?.let {
                topRatedAdapter.setData(it.take(10))
            }
        }
    }


    override fun onMovieClick(movie: Movie) {
        Log.d("HomeActivity", "Clicked on movie: ${movie.title} (ID: ${movie.id})")
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }
    // hàm hiển thị hộp thoại
    private fun showLoginPromptDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Required")
            .setMessage("You need to be logged in to use this feature. Would you like to log in or sign up?")
            .setPositiveButton("Login / Sign Up") { dialog, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                // Xóa các màn hình phía trên để khi back từ Login sẽ thoát app
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // Khi hủy, set lại item Home được chọn
                binding.bottomNavigation.selectedItemId = R.id.nav_home
                dialog.dismiss()
            }
            .setOnCancelListener {
                // Xử lý khi người dùng nhấn nút back để tắt dialog
                binding.bottomNavigation.selectedItemId = R.id.nav_home
            }
            .create()
            .show()
    }
}