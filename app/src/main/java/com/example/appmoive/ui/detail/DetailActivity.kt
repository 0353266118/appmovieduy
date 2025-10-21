// file: ui/detail/DetailActivity.kt
package com.example.appmoive.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.MovieDetail
import com.example.appmoive.databinding.ActivityDetailBinding
import com.example.appmoive.ui.adapters.CastAdapter
import com.example.appmoive.ui.adapters.ReviewAdapter
import com.example.appmoive.utils.Constants.IMAGE_BASE_URL
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var castAdapter: CastAdapter
    private lateinit var reviewAdapter: ReviewAdapter

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
    }

    private fun setupUI() {
        // Setup RecyclerView cho diễn viên
        castAdapter = CastAdapter(emptyList())
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

        binding.ivBackArrow.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun observeViewModel() {
        viewModel.movieDetails.observe(this) { details ->
            details?.let {
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

        viewModel.cast.observe(this) { castList ->
            castList?.let {
                castAdapter.setData(it)
            }
        }

        viewModel.reviews.observe(this) { reviewList ->
            reviewList?.let {
                reviewAdapter.setData(it)
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
}