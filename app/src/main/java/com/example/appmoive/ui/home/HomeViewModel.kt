// file: ui/home/HomeViewModel.kt
package com.example.appmoive.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MovieRepository()

    // Giữ nguyên
    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    // THAY ĐỔI 1: Đổi tên LiveData cho banner
    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> = _trendingMovies

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                // Thay vì gọi getTrendingMovies, chúng ta gọi getPopularMovies
                val response = repository.getPopularMovies(page = 1)
                if (response.isSuccessful) {
                    // Cập nhật cho đúng LiveData _popularMovies
                    _popularMovies.postValue(response.body()?.movies)
                } else {
                    Log.e("HomeViewModel", "Error fetching popular movies: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
            }
        }
    }

    // THAY ĐỔI 2: Sửa lại hoàn toàn hàm fetch dữ liệu cho banner
    fun fetchTrendingMovies() { // <-- Đổi tên hàm
        viewModelScope.launch {
            try {
                val response = repository.getTrendingMovies() // <-- Gọi hàm mới của Repository
                if (response.isSuccessful) {
                    _trendingMovies.postValue(response.body()?.movies) // <-- Cập nhật cho LiveData mới
                } else {
                    Log.e("HomeViewModel", "Error fetching trending movies: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
            }
        }
    }
}