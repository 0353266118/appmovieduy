package com.example.appmoive.data.repository

import com.example.appmoive.data.api.ApiClient
import com.example.appmoive.data.model.MovieResponse
import retrofit2.Response


// nhà kho quyết định xem lấy phim từ API hay ROOM database

// Repository không cần nhận ApiService qua constructor nữa
// vì nó có thể lấy trực tiếp từ ApiClient singleton
class MovieRepository {

    suspend fun getPopularMovies(page: Int): Response<MovieResponse> {
        return ApiClient.apiService.getPopularMovies(page = page)
    }
    suspend fun getTrendingMovies(): Response<MovieResponse> { // <-- Đổi tên hàm
        return ApiClient.apiService.getTrendingMovies() // <-- Gọi hàm mới của ApiService
    }
    suspend fun getTopRatedMovies(page: Int): Response<MovieResponse> {
        return ApiClient.apiService.getTopRatedMovies(page = page)
    }
}