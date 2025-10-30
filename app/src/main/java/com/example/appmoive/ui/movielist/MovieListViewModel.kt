// file: ui/movielist/MovieListViewModel.kt
package com.example.appmoive.ui.movielist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.local.AppDatabase
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    private var currentPage = 1
    private var isFetching = false


    var listType: String = "popular"

    init {
        val favoriteMovieDao = AppDatabase.getDatabase(application).favoriteMovieDao()
        repository = MovieRepository(favoriteMovieDao)
    }

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchMovies() {
        if (isFetching) {
            return
        }
        isFetching = true
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = when (listType) {
                    "top_rated" -> repository.getTopRatedMovies(page = currentPage)
                    else -> repository.getPopularMovies(page = currentPage) // Mặc định là 'popular'
                }

                if (response.isSuccessful) {
                    response.body()?.movies?.let {
                        _movies.postValue(it)
                        currentPage++
                    }
                } else {
                    Log.e("MovieListViewModel", "Error fetching '$listType' movies: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MovieListViewModel", "Exception: ${e.message}")
            } finally {
                isFetching = false
                _isLoading.postValue(false)
            }
        }
    }
}