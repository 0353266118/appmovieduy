
package com.example.appmoive.ui.genres
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoive.R
import com.example.appmoive.data.model.Genre
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ActivityGenresBinding
import com.example.appmoive.ui.adapters.GenreChipAdapter
import com.example.appmoive.ui.adapters.MovieAdapter
import com.example.appmoive.ui.adapters.OnGenreClickListener
import com.example.appmoive.ui.adapters.OnMovieClickListener
import com.example.appmoive.ui.detail.DetailActivity
import com.example.appmoive.utils.GridSpacingItemDecoration

class GenresActivity : AppCompatActivity(), OnMovieClickListener, OnGenreClickListener {

    private lateinit var binding: ActivityGenresBinding
    private val viewModel: GenresViewModel by viewModels()
    private lateinit var genreChipAdapter: GenreChipAdapter
    private lateinit var moviesAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()

        viewModel.fetchGenres()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }


        genreChipAdapter = GenreChipAdapter(emptyList(), this)
        binding.rvGenreChips.apply {
            layoutManager = LinearLayoutManager(this@GenresActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = genreChipAdapter
        }


        moviesAdapter = MovieAdapter(emptyList(), this)
        binding.rvGenreMovies.apply {
            val gridLayoutManager = GridLayoutManager(this@GenresActivity, 2)
            layoutManager = gridLayoutManager
            adapter = moviesAdapter


            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
            addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = gridLayoutManager.itemCount
                    val lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()

                    val genreId = viewModel.currentGenreId


                    if (genreId != null && totalItemCount <= lastVisibleItemPosition + 4) {
                        viewModel.fetchMoviesByGenre(genreId)
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.genres.observe(this) { genres ->
            genres?.let {
                genreChipAdapter.setData(it)

                if (it.isNotEmpty()) {
                    onGenreClick(it.first())
                }
            }
        }


        viewModel.moviesByGenre.observe(this) { movies ->
            movies?.let {
                moviesAdapter.setData(it)
            }
        }
    }


    override fun onGenreClick(genre: Genre) {
        viewModel.fetchMoviesByGenre(genre.id)
        binding.rvGenreMovies.scrollToPosition(0)
    }


    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }
}