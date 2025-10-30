package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoive.data.model.Genre
import com.example.appmoive.databinding.ItemGenreChipBinding

class GenreChipAdapter(
    private var genres: List<Genre>,
    private val listener: OnGenreClickListener
) : RecyclerView.Adapter<GenreChipAdapter.GenreViewHolder>() {

    private var selectedPosition = 0 // Mặc định chọn item đầu tiên

    inner class GenreViewHolder(val binding: ItemGenreChipBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun getItemCount(): Int = genres.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.binding.chipGenre.text = genre.name

        // Cập nhật trạng thái được chọn
        holder.binding.chipGenre.isChecked = (position == selectedPosition)

        holder.binding.chipGenre.setOnClickListener {
            if (selectedPosition != position) {
                // Cập nhật vị trí mới và thông báo cho adapter vẽ lại
                val oldPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)

                // Gọi listener để báo cho Activity biết
                listener.onGenreClick(genre)
            }
        }
    }

    fun setData(newGenres: List<Genre>) {
        genres = newGenres
        selectedPosition = 0
        notifyDataSetChanged()
    }
}