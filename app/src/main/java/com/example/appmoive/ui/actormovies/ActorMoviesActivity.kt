// file: ui/actormovies/ActorMoviesActivity.kt
package com.example.appmoive.ui.actormovies

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivityActorMoviesBinding
import com.example.appmoive.ui.adapters.MovieListAdapter
import com.example.appmoive.ui.adapters.OnMovieClickListener // <<-- ĐẢM BẢO IMPORT
import com.example.appmoive.ui.detail.DetailActivity
import com.example.appmoive.utils.Constants

// SỬA: Implement interface
class ActorMoviesActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var binding: ActivityActorMoviesBinding
    private val viewModel: ActorMoviesViewModel by viewModels()
    private lateinit var moviesAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActorMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val actorId = intent.getIntExtra("ACTOR_ID", -1)
        if (actorId == -1) {
            finish(); return
        }

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchAllActorData(actorId)
    }

    private fun setupRecyclerView() {
        // SỬA: Truyền "this" vào làm listener
        moviesAdapter = MovieListAdapter(mutableListOf(), this)
        binding.rvActorMovies.apply {
            layoutManager = LinearLayoutManager(this@ActorMoviesActivity)
            adapter = moviesAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.actorDetails.observe(this) { actor ->
            actor?.let {
                // SỬA 1: Chỉ cập nhật tên trên toolbar (khi co lại). Tên lớn đã có sẵn.
                // Nếu muốn tên lớn trên ảnh, chúng ta sẽ làm cách khác.
                binding.collapsingToolbar.title = it.name

                // Cập nhật tên lớn (khi mở rộng)
//                binding.tvActorNameLarge.text = it.name

                // SỬA 2: Thêm text nhãn "Birthday: " và "Place of Birth: "
                val birthdayText = "Birthday: ${it.birthday ?: "N/A"}"
                binding.tvActorBirthday.text = birthdayText

                val pobText = "Place of Birth: ${it.placeOfBirth ?: "N/A"}"
                binding.tvActorNationality.text = pobText

                Glide.with(this).load(Constants.IMAGE_BASE_URL + it.profilePath)
                    .into(binding.ivActorPhoto)
            }
        }

        viewModel.actorMovies.observe(this) { movies ->
            movies?.let {
                moviesAdapter.addMovies(it)
            }
        }
    }

    // SỬA: Hàm override phải nằm ở đây
    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }
}