// file: ui/genres/GenresViewModel.kt
package com.example.appmoive.ui.genres

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.local.AppDatabase
import com.example.appmoive.data.model.Genre
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class GenresViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    // LiveData cho danh sách thể loại
    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> = _genres

    // LiveData cho danh sách phim theo thể loại (sẽ được cập nhật liên tục)
    private val _moviesByGenre = MutableLiveData<List<Movie>>()
    val moviesByGenre: LiveData<List<Movie>> = _moviesByGenre

    // Các biến để quản lý trạng thái cuộn vô hạn
    private var movieList = mutableListOf<Movie>()
    private var currentPage = 1
    private var isFetching = false
    var currentGenreId: Int? = null // Activity sẽ cần truy cập biến này
        private set // Chỉ cho phép ViewModel thay đổi giá trị này

    init {
        val dao = AppDatabase.getDatabase(application).favoriteMovieDao()
        repository = MovieRepository(dao)
    }

    fun fetchGenres() {
        viewModelScope.launch {
            val response = repository.getMovieGenres()
            if (response.isSuccessful) {
                _genres.postValue(response.body()?.genres)
            }
        }
    }

    fun fetchMoviesByGenre(genreId: Int) {
        // Nếu đang fetch rồi thì không làm gì cả
        if (isFetching) return

        // Nếu người dùng chọn một thể loại mới, reset lại mọi thứ
        if (currentGenreId != genreId) {
            currentPage = 1
            currentGenreId = genreId
            movieList.clear()
        }

        isFetching = true
        // Có thể thêm LiveData cho isLoading ở đây để hiển thị ProgressBar

        viewModelScope.launch {
            try {
                val response = repository.discoverMoviesByGenre(genreId, currentPage)
                if (response.isSuccessful) {
                    response.body()?.movies?.let { newMovies ->
                        movieList.addAll(newMovies)
                        // Cập nhật LiveData với một bản sao của danh sách hiện tại
                        _moviesByGenre.postValue(movieList.toList())
                        currentPage++
                    }
                } else {
                    Log.e("GenresViewModel", "Error fetching movies for genre $genreId: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("GenresViewModel", "Exception: ${e.message}")
            } finally {
                // Dù thành công hay thất bại cũng phải hạ cờ
                isFetching = false
            }
        }
    }
}