// file: ui/favorites/FavoritesViewModel.kt
package com.example.appmoive.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.local.AppDatabase
import com.example.appmoive.data.model.FavoriteMovie
import com.example.appmoive.data.repository.MovieRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    private val userId: String? // Chỉ khai báo ở đây

    // SỬA: Khai báo LiveData ở đây
    val allFavoriteMovies: LiveData<List<FavoriteMovie>>

    init {
        val favoriteMovieDao = AppDatabase.getDatabase(application).favoriteMovieDao()
        repository = MovieRepository(favoriteMovieDao)

        // Gán giá trị cho userId một lần duy nhất
        userId = FirebaseAuth.getInstance().currentUser?.uid

        // Gán giá trị cho LiveData sau khi repository đã được khởi tạo
        allFavoriteMovies = if (userId != null) {
            repository.getAllFavoriteMovies(userId).asLiveData()
        } else {
            MutableLiveData(emptyList()) // Trả về LiveData rỗng nếu không có user
        }
    }

    // Hàm này trong FavoritesViewModel là không cần thiết
    // vì hàm delete trong repository đã có, và danh sách sẽ tự cập nhật nhờ Flow
    // Tuy nhiên, nếu FavoritesAdapter gọi đến nó thì cứ giữ lại
    fun removeFromFavorites(movie: FavoriteMovie) {
        viewModelScope.launch {
            // Đảm bảo movie được xóa có userId đúng
            if (userId != null && movie.userId == userId) {
                repository.removeFromFavorites(movie)
            }
        }
    }
}