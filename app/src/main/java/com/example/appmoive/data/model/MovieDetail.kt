package com.example.appmoive.data.model

import com.google.gson.annotations.SerializedName


// chi tiết về 1 phim
data class MovieDetail(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    val runtime: Int?,
    val overview: String,
    val genres: List<Genre>

)