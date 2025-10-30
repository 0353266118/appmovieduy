package com.example.appmoive.ui.adapters


// interface sử lí sự kiện khi click vào item phim trong danh sách yêu thích và icon trái tim

import com.example.appmoive.data.model.FavoriteMovie

interface OnFavoriteClickListener {
    fun onFavoriteMovieClick(movie: FavoriteMovie)
    fun onRemoveFavoriteClick(movie: FavoriteMovie)
}