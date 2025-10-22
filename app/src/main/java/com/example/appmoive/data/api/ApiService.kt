package com.example.appmoive.data.api

import com.example.appmoive.data.model.ActorDetail
import com.example.appmoive.data.model.ActorMoviesResponse
import com.example.appmoive.data.model.CreditsResponse
import com.example.appmoive.data.model.MovieDetail
import com.example.appmoive.data.model.MovieResponse
import com.example.appmoive.data.model.ReviewsResponse
import com.example.appmoive.data.model.VideosResponse
import com.example.appmoive.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieDetail>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<CreditsResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<ReviewsResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<VideosResponse>

    // MỚI: Endpoint để lấy phim liên quan
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse> // Tái sử dụng MovieResponse vì cấu trúc JSON giống nhau

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<ActorMoviesResponse>


    @GET("person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<ActorDetail>


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") query: String, // Từ khóa tìm kiếm
        @Query("page") page: Int
    ): Response<MovieResponse>
}