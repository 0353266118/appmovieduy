package com.example.appmoive.data.model
import com.google.gson.annotations.SerializedName


// danh sách phim được trả về
data class MovieResponse(
    @SerializedName("results")
    val movies: List<Movie>
)