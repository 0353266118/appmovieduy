package com.example.appmoive.data.model
import com.google.gson.annotations.SerializedName
data class Movie(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("vote_average")
    val voteAverage: Double,


    @SerializedName("backdrop_path")
    val backdropPath: String?, // Dùng String? vì có thể một số phim không có backdrop

    @SerializedName("overview")
    val overview: String


)
