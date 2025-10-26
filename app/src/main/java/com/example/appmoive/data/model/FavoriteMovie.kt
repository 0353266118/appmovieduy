// file: data/model/FavoriteMovie.kt
package com.example.appmoive.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// SỬA 1: Khai báo khóa chính σύνθετη
@Entity(tableName = "favorite_movies", primaryKeys = ["id", "userId"])
data class FavoriteMovie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String,

    // SỬA 2: Thêm trường để lưu ID của người dùng
    val userId: String
)