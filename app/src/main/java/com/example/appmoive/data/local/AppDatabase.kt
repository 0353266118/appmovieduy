package com.example.appmoive.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appmoive.data.model.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 2, exportSchema = false) // <<-- TĂNG VERSION LÊN 2
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao(): FavoriteMovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,

                    AppDatabase::class.java,
                    "movie_database"
                )
                    .fallbackToDestructiveMigration() // <<-- THÊM DÒNG NÀY
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}