// file: ui/home/HomeViewModel.kt
package com.example.appmoive.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.local.AppDatabase
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    init {
        val favoriteMovieDao = AppDatabase.getDatabase(application).favoriteMovieDao()
        repository = MovieRepository(favoriteMovieDao)
    }

    // Giữ nguyên
    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    //  Đổi tên LiveData cho banner
    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> = _trendingMovies

    // Thêm LiveData cho danh sách Top Rated
    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>> = _topRatedMovies

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(page = 1)
                if (response.isSuccessful) {

                    _popularMovies.postValue(response.body()?.movies)
                } else {
                    Log.e("HomeViewModel", "Error fetching popular movies: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
            }
        }
    }


    fun fetchTrendingMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getTrendingMovies()
                if (response.isSuccessful) {
                    _trendingMovies.postValue(response.body()?.movies)
                } else {
                    Log.e("HomeViewModel", "Error fetching trending movies: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
            }
        }
    }

    //  hàm fetch phim đánh giá cao
    fun fetchTopRatedMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getTopRatedMovies(page = 1)
                if (response.isSuccessful) {
                    _topRatedMovies.postValue(response.body()?.movies)
                } else {
                    Log.e("HomeViewModel", "Error fetching top rated movies: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception fetching top rated: ${e.message}")
            }
        }
    }
}