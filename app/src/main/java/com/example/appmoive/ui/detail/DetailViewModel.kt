// file: ui/detail/DetailViewModel.kt
package com.example.appmoive.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.appmoive.data.local.AppDatabase
import com.example.appmoive.data.model.*
import com.example.appmoive.data.repository.MovieRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// Kế thừa từ AndroidViewModel(application)
class DetailViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: MovieRepository
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        val favoriteMovieDao = AppDatabase.getDatabase(application).favoriteMovieDao()
        repository = MovieRepository(favoriteMovieDao)
    }

    private val _movieDetails = MutableLiveData<MovieDetail>()
    val movieDetails: LiveData<MovieDetail> = _movieDetails

    private val _cast = MutableLiveData<List<Cast>>()
    val cast: LiveData<List<Cast>> = _cast

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private val _trailerKey = MutableLiveData<String?>()
    val trailerKey: LiveData<String?> = _trailerKey

    private val _similarMovies = MutableLiveData<List<Movie>>()
    val similarMovies: LiveData<List<Movie>> = _similarMovies

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite





    fun fetchAllData(movieId: Int) {
        viewModelScope.launch {
            if (userId != null) {
                _isFavorite.postValue(repository.isFavorite(movieId, userId))
            }

            val detailsResponse = repository.getMovieDetails(movieId)
            if (detailsResponse.isSuccessful) {
                _movieDetails.postValue(detailsResponse.body())
            }

            val creditsResponse = repository.getMovieCredits(movieId)
            if (creditsResponse.isSuccessful) {
                _cast.postValue(creditsResponse.body()?.cast)
            }

            val reviewsResponse = repository.getMovieReviews(movieId)
            if (reviewsResponse.isSuccessful) {
                _reviews.postValue(reviewsResponse.body()?.results)
            }

            val videosResponse = repository.getMovieVideos(movieId)
            if (videosResponse.isSuccessful) {
                val officialTrailer = videosResponse.body()?.results?.find { it.site == "YouTube" && it.type == "Trailer" }
                _trailerKey.postValue(officialTrailer?.key)
            }

            val similarResponse = repository.getSimilarMovies(movieId)
            if (similarResponse.isSuccessful) {
                _similarMovies.postValue(similarResponse.body()?.movies)
            }
        }
    }

    fun addToFavorites(movie: MovieDetail) {
        viewModelScope.launch {
            if (userId != null) {
                // SỬA LẠI DÒNG NÀY
                val favoriteMovie = FavoriteMovie(
                    id = movie.id,
                    title = movie.title,
                    posterPath = movie.posterPath ?: "",
                    overview = movie.overview,
                    userId = userId
                )
                repository.addToFavorites(favoriteMovie)
                _isFavorite.postValue(true)
            }
        }
    }
    fun removeFromFavorites(movie: MovieDetail) {
        viewModelScope.launch {
            if (userId != null) {
                // SỬA LẠI DÒNG NÀY
                val favoriteMovie = FavoriteMovie(
                    id = movie.id,
                    title = movie.title,
                    posterPath = movie.posterPath ?: "",
                    overview = movie.overview,
                    userId = userId
                )
                repository.removeFromFavorites(favoriteMovie)
                _isFavorite.postValue(false)
            }
        }
    }
}