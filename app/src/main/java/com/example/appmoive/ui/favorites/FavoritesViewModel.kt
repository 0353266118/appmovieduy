// file: ui/favorites/FavoritesViewModel.kt
package com.example.appmoive.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.local.AppDatabase
import com.example.appmoive.data.model.FavoriteMovie
import com.example.appmoive.data.repository.MovieRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    private val userId: String?


    val allFavoriteMovies: LiveData<List<FavoriteMovie>>

    init {
        val favoriteMovieDao = AppDatabase.getDatabase(application).favoriteMovieDao()
        repository = MovieRepository(favoriteMovieDao)


        userId = FirebaseAuth.getInstance().currentUser?.uid


        allFavoriteMovies = if (userId != null) {
            repository.getAllFavoriteMovies(userId).asLiveData()
        } else {
            MutableLiveData(emptyList())
        }
    }

    fun removeFromFavorites(movie: FavoriteMovie) {
        viewModelScope.launch {
            if (userId != null && movie.userId == userId) {
                repository.removeFromFavorites(movie)
            }
        }
    }
}