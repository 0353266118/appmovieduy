package com.example.appmoive.data.model


// API trả về danh sách phim mà diễn viên đóng
// file: data/model/ActorMoviesResponse.kt
data class ActorMoviesResponse(
    val cast: List<Movie> // Tái sử dụng Movie model
)