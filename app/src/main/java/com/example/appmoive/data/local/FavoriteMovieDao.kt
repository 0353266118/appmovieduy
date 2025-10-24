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

    @Query("SELECT * FROM favorite_movies")
    fun getAllFavoriteMovies(): Flow<List<FavoriteMovie>> // Dùng Flow để tự động cập nhật UI

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteMovieById(movieId: Int): FavoriteMovie?
}