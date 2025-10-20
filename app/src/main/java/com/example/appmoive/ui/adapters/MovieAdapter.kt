// file: ui/adapters/MovieAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmoive.data.model.Movie
import com.example.appmoive.databinding.ItemMoviePosterBinding // <-- Đảm bảo tên file layout của em được map đúng
import com.example.appmoive.utils.Constants.IMAGE_BASE_URL

// Giả sử tên file layout của em là item_movie_poster.xml,
// ViewBinding sẽ tự động tạo ra lớp ItemMoviePosterBinding

class MovieAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    // ViewHolder không có gì thay đổi, ViewBinding đã lo hết rồi
    inner class MovieViewHolder(val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // Chỗ này cũng không đổi
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    // **** PHẦN QUAN TRỌNG NHẤT LÀ Ở ĐÂY ****
    // Chúng ta sẽ cập nhật hàm onBindViewHolder
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.apply {
            // 1. Cập nhật ID Poster: như em đã làm, đổi thành ivPoster
            Glide.with(root.context)
                .load(IMAGE_BASE_URL + movie.posterPath)
                .into(ivPoster) // Sửa thành ID mới trong XML

            // 2. Hiển thị Tên Phim: Lấy dữ liệu từ movie.title
            tvMovieTitle.text = movie.title

            // 3. Hiển thị Thể Loại Phim (Lưu ý quan trọng)
            // Hiện tại, model Movie của chúng ta chưa có thông tin về tên thể loại.
            // API của TMDb trả về một danh sách các ID của thể loại.
            // Để đơn giản và có cái hiển thị trước, chúng ta sẽ tạm thời để một dòng chữ cứng.
            // Ở các bước nâng cao sau, anh sẽ chỉ em cách gọi API để lấy tên thể loại từ ID.
            tvMovieGenre.text = "Action, Adventure" // <-- Dữ liệu giả (placeholder)
        }
    }
    fun setData(newMovies: List<Movie>) {
        this.movies = newMovies
        notifyDataSetChanged() // Báo cho RecyclerView biết dữ liệu đã thay đổi để vẽ lại
    }
}