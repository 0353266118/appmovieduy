// file: ui/adapters/ReviewAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoive.data.model.Review
import com.example.appmoive.databinding.ItemReviewBinding

class ReviewAdapter(private var reviewList: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int = reviewList.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.binding.apply {
            tvReviewAuthor.text = review.author
            tvReviewContent.text = review.content


            val ratingValue = review.authorDetails.rating
            if (ratingValue != null) {
                rbReviewRating.visibility = View.VISIBLE

                rbReviewRating.rating = ratingValue.toFloat()
            } else {

                rbReviewRating.visibility = View.GONE
            }
        }
    }

    fun setData(newReviewList: List<Review>) {
        this.reviewList = newReviewList
        notifyDataSetChanged()
    }
}