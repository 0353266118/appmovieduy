// file: data/api/ApiService.kt
package com.example.appmoive.data.api

import com.example.appmoive.data.model.*
import com.example.appmoive.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


// interface định nghĩa các lệnh api gọi đến sever
interface ApiService {

    // --- API cho danh sách phim ---
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<MovieResponse>

    // SỬA LẠI: Bỏ tham số 'page'
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(@Query("api_key") apiKey: String = Constants.API_KEY): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun discoverMoviesByGenre(@Query("with_genres") genreId: Int, @Query("page") page: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<MovieResponse>

    // --- API cho chi tiết phim ---
    // SỬA LẠI: Đảm bảo các ID là Int
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<MovieDetail>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<CreditsResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<ReviewsResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<VideosResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<MovieResponse>

    // --- API cho diễn viên ---
    // SỬA LẠI: Đảm bảo các ID là Int
    @GET("person/{person_id}")
    suspend fun getActorDetails(@Path("person_id") personId: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<ActorDetail>

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(@Path("person_id") personId: Int, @Query("api_key") apiKey: String = Constants.API_KEY): Response<ActorMoviesResponse>

    // --- API cho thể loại ---
    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("api_key") apiKey: String = Constants.API_KEY): Response<GenresResponse>
}