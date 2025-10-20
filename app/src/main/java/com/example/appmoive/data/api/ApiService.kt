package com.example.appmoive.data.api

import com.example.appmoive.data.model.MovieResponse
import com.example.appmoive.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
// interface định nghĩa các lệnh API đến trang TMDB
interface ApiService {

    // Endpoint để lấy danh sách phim phổ biến
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int // Thêm tham số trang
    ): Response<MovieResponse>

    @GET("trending/movie/day") // <-- Sửa endpoint
    suspend fun getTrendingMovies( // <-- Đổi tên hàm cho rõ nghĩa
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Response<MovieResponse>


}