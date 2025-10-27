// file: ui/detail/DetailActivity.kt
package com.example.appmoive.ui.detail
import android.content.Intent
import android.net.Uri

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appmoive.R
import com.example.appmoive.data.model.Cast
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.model.MovieDetail
import com.example.appmoive.databinding.ActivityDetailBinding
import com.example.appmoive.ui.actormovies.ActorMoviesActivity
import com.example.appmoive.ui.adapters.CastAdapter
import com.example.appmoive.ui.adapters.MovieAdapter
import com.example.appmoive.ui.adapters.OnMovieClickListener
import com.example.appmoive.ui.adapters.ReviewAdapter
import com.example.appmoive.utils.Constants.IMAGE_BASE_URL
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.appcompat.app.AlertDialog // <<-- THÊM IMPORT
import com.example.appmoive.ui.auth.LoginActivity // <<-- THÊM IMPORT
import com.google.firebase.auth.FirebaseAuth // <<-- THÊM IMPORT

import com.example.appmoive.ui.adapters.OnActorClickListener



class DetailActivity : AppCompatActivity(), OnMovieClickListener, OnActorClickListener {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var castAdapter: CastAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var similarMoviesAdapter: MovieAdapter // Tái sử dụng MovieAdapter
    private lateinit var firebaseAuth: FirebaseAuth // <<-- THÊM BIẾN NÀY


    // Biến để lưu trữ key của trailer
    private var currentTrailerKey: String? = null

    private var currentMovieDetail: MovieDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId == -1) {
            finish()
            return
        }

        setupUI()
        observeViewModel()

        viewModel.fetchAllData(movieId)
        firebaseAuth = FirebaseAuth.getInstance() // <<-- KHỞI TẠO
    }

    private fun setupUI() {
        // Setup RecyclerView cho diễn viên
        castAdapter = CastAdapter(emptyList(), this)
        binding.rvCast.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }

        // Setup RecyclerView cho reviews
        reviewAdapter = ReviewAdapter(emptyList())
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = reviewAdapter
        }
        binding.btnPlayTrailer.setOnClickListener {
            currentTrailerKey?.let { key ->
                playYouTubeTrailer(key)
            }
        }
        binding.ivFavorite.setOnClickListener {
            if (firebaseAuth.currentUser != null) {
                // Nếu đã đăng nhập, thực hiện logic như cũ
                currentMovieDetail?.let { movie ->
                    if (viewModel.isFavorite.value == true) {
                        viewModel.removeFromFavorites(movie)
                    } else {
                        viewModel.addToFavorites(movie)
                    }
                }
            } else {
                // Nếu chưa đăng nhập, hiển thị hộp thoại
                showLoginPromptDialog()
            }
        }

        binding.ivBackArrow.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // MỚI: Setup RecyclerView cho phim liên quan
        similarMoviesAdapter = MovieAdapter(emptyList(), this) // Tái sử dụng và truyền listener
        binding.rvSimilarMovies.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarMoviesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.movieDetails.observe(this) { details ->
            details?.let {
                currentMovieDetail = it
                binding.apply {
                    Glide.with(this@DetailActivity).load(IMAGE_BASE_URL + it.backdropPath).into(ivBackdrop)
                    tvTitleDetail.text = it.title
                    tvRating.text = String.format("%.1f", it.voteAverage)

                    // =======================================================
                    // SỬA LẠI PHẦN XỬ LÝ NGÀY THÁNG Ở ĐÂY
                    // =======================================================
                    tvReleaseDate.text = formatReleaseDate(it.releaseDate)

                    tvGenres.text = it.genres.joinToString(", ") { genre -> genre.name }
                    tvOverview.text = it.overview
                    tvRuntime.text = formatRuntime(it.runtime)
                }
            }
        }
        // MỚI: Lắng nghe trailerKey
        viewModel.trailerKey.observe(this) { key ->
            currentTrailerKey = key
            if (key != null) {
                // Nếu có trailer, hiện nút Play lên
                binding.btnPlayTrailer.visibility = View.VISIBLE
            } else {
                // Nếu không có, ẩn nút đi
                binding.btnPlayTrailer.visibility = View.GONE
            }
        }

        viewModel.cast.observe(this) { castList ->
            castList?.let {
                castAdapter.setData(it)
            }
        }

        viewModel.reviews.observe(this) { reviewList ->
            if (reviewList.isNullOrEmpty()) {
                // Logic này vẫn đúng: Ẩn cả tiêu đề và container
                binding.groupReviews.visibility = View.GONE
            } else {
                // Hiện lên và đổ dữ liệu
                binding.groupReviews.visibility = View.VISIBLE
                reviewAdapter.setData(reviewList)
            }
        }
        viewModel.similarMovies.observe(this) { movies ->
            movies?.let {
                similarMoviesAdapter.setData(it)
            }
        }
        viewModel.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

    }
    private fun formatRuntime(minutes: Int?): String {
        if (minutes == null || minutes <= 0) {
            return "N/A"
        }
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h ${remainingMinutes}m"
    }
    private fun formatReleaseDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) {
            return "N/A" // Trả về "Not Available" nếu không có ngày
        }
        try {
            // Định dạng đầu vào từ API (YYYY-MM-DD)
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // Định dạng đầu ra mong muốn (DD/MM/YYYY)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            return if (date != null) outputFormat.format(date) else dateString
        } catch (e: Exception) {
            // Nếu có lỗi parse, trả về chuỗi gốc
            return dateString
        }
    }
    private fun playYouTubeTrailer(videoKey: String) {
        // Tạo Intent để mở ứng dụng YouTube
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoKey"))
        // Tạo Intent để mở trên trình duyệt web nếu không có ứng dụng YouTube
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoKey"))

        try {
            // Ưu tiên mở bằng app
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            // Nếu không có app, mở bằng trình duyệt
            startActivity(webIntent)
        }
    }
    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }
    // SỬA: Hàm override cho click diễn viên
    override fun onActorClick(actor: Cast) {
        val intent = Intent(this, ActorMoviesActivity::class.java).apply {
            putExtra("ACTOR_ID", actor.id)
            putExtra("ACTOR_NAME", actor.name)
        }
        startActivity(intent)
    }
    // MỚI: Thêm hàm hiển thị hộp thoại
    private fun showLoginPromptDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Required")
            .setMessage("You need to be logged in to save favorite movies.")
            .setPositiveButton("Login / Sign Up") { dialog, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}

