package com.example.appmoive.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmoive.data.model.Movie
import com.example.appmoive.data.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    fun searchMovies(query: String) {
        if (query.isBlank()) {
            _searchResults.postValue(emptyList()) // Xóa kết quả nếu query trống
            return
        }

        viewModelScope.launch {
            // Tạm thời chỉ tìm kiếm trang đầu tiên
            val response = repository.searchMovies(query, 1)
            if (response.isSuccessful) {
                _searchResults.postValue(response.body()?.movies)
            }
        }
    }
}