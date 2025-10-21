package com.example.appmoive.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.model.Cast
import com.example.appmoive.data.model.Movie
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
    // MỚI: LiveData để lưu key của video trailer
    private val _trailerKey = MutableLiveData<String?>()
    val trailerKey: LiveData<String?> = _trailerKey

    private val _similarMovies = MutableLiveData<List<Movie>>()
    val similarMovies: LiveData<List<Movie>> = _similarMovies

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

            // Lấy danh sách video
            val videosResponse = repository.getMovieVideos(movieId)
            if (videosResponse.isSuccessful) {
                // Tìm video đầu tiên là "Trailer" trên trang "YouTube"
                val officialTrailer = videosResponse.body()?.results?.find { video ->
                    video.site == "YouTube" && video.type == "Trailer"
                }
                // Cập nhật LiveData với key của trailer tìm được (hoặc null nếu không có)
                _trailerKey.postValue(officialTrailer?.key)
            }

            // MỚI: Lấy danh sách phim liên quan
            val similarResponse = repository.getSimilarMovies(movieId)
            if (similarResponse.isSuccessful) {
                _similarMovies.postValue(similarResponse.body()?.movies)
            }

        }
    }
}