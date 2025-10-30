// file: ui/actormovies/ActorMoviesViewModel.kt
package com.example.appmoive.ui.actormovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.local.AppDatabase
import com.example.appmoive.data.model.ActorDetail
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class ActorMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    init {
        val favoriteMovieDao = AppDatabase.getDatabase(application).favoriteMovieDao()

        repository = MovieRepository(favoriteMovieDao)
    }

    // LiveData cho chi tiết diễn viên
    private val _actorDetails = MutableLiveData<ActorDetail>()
    val actorDetails: LiveData<ActorDetail> = _actorDetails

    // LiveData cho danh sách phim
    private val _actorMovies = MutableLiveData<List<Movie>>()
    val actorMovies: LiveData<List<Movie>> = _actorMovies

    fun fetchAllActorData(actorId: Int) {
        viewModelScope.launch {
            // Lấy chi tiết
            val detailsResponse = repository.getActorDetails(actorId)
            if (detailsResponse.isSuccessful) {
                _actorDetails.postValue(detailsResponse.body())
            }

            // Lấy danh sách phim
            val moviesResponse = repository.getActorMovies(actorId)
            if (moviesResponse.isSuccessful) {
                _actorMovies.postValue(moviesResponse.body()?.cast)
            }
        }
    }
}