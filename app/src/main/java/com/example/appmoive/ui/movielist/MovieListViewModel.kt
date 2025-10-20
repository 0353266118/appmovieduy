// file: ui/movielist/MovieListViewModel.kt
package com.example.appmoive.ui.movielist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {
    private val repository = MovieRepository()
    private var currentPage = 1

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // SỬA LỖI: Thêm một biến cờ để kiểm soát việc gọi API
    private var isFetching = false

    fun fetchPopularMovies() {
        // KIỂM TRA CỜ: Nếu đang fetch rồi thì không làm gì cả
        if (isFetching) {
            return
        }

        // ĐẶT CỜ: Đánh dấu là bắt đầu fetch
        isFetching = true
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(page = currentPage)
                if (response.isSuccessful) {
                    response.body()?.movies?.let {
                        _movies.postValue(it)
                        currentPage++ // Chỉ tăng trang khi gọi API thành công
                    }
                } else {
                    Log.e("MovieListViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MovieListViewModel", "Exception: ${e.message}")
            } finally {
                // HẠ CỜ: Dù thành công hay thất bại, cuối cùng cũng phải hạ cờ xuống
                // để cho phép lần gọi tiếp theo
                isFetching = false
                _isLoading.postValue(false)
            }
        }
    }
}