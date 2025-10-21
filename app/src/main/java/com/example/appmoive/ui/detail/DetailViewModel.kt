package com.example.appmoive.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.model.Cast
import com.example.appmoive.data.model.MovieDetail
import com.example.appmoive.data.model.Review
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

// file: ui/detail/DetailViewModel.kt
class DetailViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movieDetails = MutableLiveData<MovieDetail>()
    val movieDetails: LiveData<MovieDetail> = _movieDetails

    private val _cast = MutableLiveData<List<Cast>>()
    val cast: LiveData<List<Cast>> = _cast

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    fun fetchAllData(movieId: Int) {
        viewModelScope.launch {
            // Lấy chi tiết phim
            val detailsResponse = repository.getMovieDetails(movieId)
            if (detailsResponse.isSuccessful) {
                _movieDetails.postValue(detailsResponse.body())
            }

            // Lấy danh sách diễn viên
            val creditsResponse = repository.getMovieCredits(movieId)
            if (creditsResponse.isSuccessful) {
                _cast.postValue(creditsResponse.body()?.cast)
            }


            // Lấy danh sách reviews
            val reviewsResponse = repository.getMovieReviews(movieId)
            if (reviewsResponse.isSuccessful) {
                _reviews.postValue(reviewsResponse.body()?.results)
            }
        }
    }
}