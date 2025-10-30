// file: data/repository/MovieRepository.kt
package com.example.appmoive.data.repository

import com.example.appmoive.data.api.ApiClient
import com.example.appmoive.data.local.FavoriteMovieDao
import com.example.appmoive.data.model.FavoriteMovie
import com.example.appmoive.data.model.MovieResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


// nơi quyết định xem lấy dữ liệu từ API hay Database
class MovieRepository(private val favoriteMovieDao: FavoriteMovieDao) {

    // --- Hàm gọi API Phim ---
    suspend fun getPopularMovies(page: Int): Response<MovieResponse> = ApiClient.apiService.getPopularMovies(page = page)


    suspend fun getTrendingMovies(): Response<MovieResponse> = ApiClient.apiService.getTrendingMovies()

    suspend fun getTopRatedMovies(page: Int): Response<MovieResponse> = ApiClient.apiService.getTopRatedMovies(page = page)

    suspend fun searchMovies(query: String, page: Int) = ApiClient.apiService.searchMovies(query = query, page = page)

    suspend fun getMovieDetails(movieId: Int) = ApiClient.apiService.getMovieDetails(movieId)
    suspend fun getMovieCredits(movieId: Int) = ApiClient.apiService.getMovieCredits(movieId)
    suspend fun getMovieReviews(movieId: Int) = ApiClient.apiService.getMovieReviews(movieId)
    suspend fun getMovieVideos(movieId: Int) = ApiClient.apiService.getMovieVideos(movieId)
    suspend fun getSimilarMovies(movieId: Int) = ApiClient.apiService.getSimilarMovies(movieId)

    // --- Hàm gọi API Diễn viên ---
    suspend fun getActorMovies(personId: Int) = ApiClient.apiService.getActorMovies(personId)
    suspend fun getActorDetails(personId: Int) = ApiClient.apiService.getActorDetails(personId)

    // --- Hàm gọi API Thể loại ---
    suspend fun getMovieGenres() = ApiClient.apiService.getMovieGenres()
    suspend fun discoverMoviesByGenre(genreId: Int, page: Int) = ApiClient.apiService.discoverMoviesByGenre(genreId, page)

    // --- Hàm tương tác với Database (Room) ---
    fun getAllFavoriteMovies(userId: String): Flow<List<FavoriteMovie>> = favoriteMovieDao.getAllFavoriteMovies(userId)

    suspend fun isFavorite(movieId: Int, userId: String): Boolean = favoriteMovieDao.getFavoriteMovieById(movieId, userId) != null

    suspend fun addToFavorites(movie: FavoriteMovie) = favoriteMovieDao.insert(movie)

    suspend fun removeFromFavorites(movie: FavoriteMovie) = favoriteMovieDao.delete(movie)
}