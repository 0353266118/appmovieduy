package com.example.appmoive.data.local

import androidx.room.*
import com.example.appmoive.data.model.FavoriteMovie
import kotlinx.coroutines.flow.Flow

// DAO định nghĩa các hành động với dữ liệu từ local database
// CRUD -create, read,update, delete
@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMovie)

    @Delete
    suspend fun delete(movie: FavoriteMovie)

    // SỬA: Thêm điều kiện userId vào câu lệnh SELECT
    @Query("SELECT * FROM favorite_movies WHERE userId = :userId")
    fun getAllFavoriteMovies(userId: String): Flow<List<FavoriteMovie>>

    // SỬA: Thêm điều kiện userId vào câu lệnh SELECT
    @Query("SELECT * FROM favorite_movies WHERE id = :movieId AND userId = :userId")
    suspend fun getFavoriteMovieById(movieId: Int, userId: String): FavoriteMovie?
}