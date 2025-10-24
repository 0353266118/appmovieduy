// file: data/model/FavoriteMovie.kt
package com.example.appmoive.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String // Thêm các trường cần thiết để hiển thị trong danh sách
)