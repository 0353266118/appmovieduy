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
import com.example.appmoive.ui.adapters.OnMovieClickListener
import com.example.appmoive.ui.detail.DetailActivity
import com.example.appmoive.utils.Constants


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

                binding.collapsingToolbar.title = it.name


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


    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }
}