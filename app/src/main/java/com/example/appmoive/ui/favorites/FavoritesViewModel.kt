// file: ui/favorites/FavoritesViewModel.kt
package com.example.appmoive.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.local.AppDatabase

import com.example.appmoive.data.model.FavoriteMovie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    init {
        val favoriteMovieDao = AppDatabase.getDatabase(application).favoriteMovieDao()
        repository = MovieRepository(favoriteMovieDao)
    }

    // Lấy danh sách phim yêu thích và chuyển đổi từ Flow sang LiveData
    // LiveData này sẽ tự động cập nhật mỗi khi database thay đổi
    val allFavoriteMovies: LiveData<List<FavoriteMovie>> = repository.getAllFavoriteMovies().asLiveData()

    fun removeFromFavorites(movie: FavoriteMovie) {
        viewModelScope.launch {
            repository.removeFromFavorites(movie)
        }
    }
}