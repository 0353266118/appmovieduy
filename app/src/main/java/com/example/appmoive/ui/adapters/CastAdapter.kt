// file: ui/adapters/CastAdapter.kt
package com.example.appmoive.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmoive.R
import com.example.appmoive.data.model.Cast
import com.example.appmoive.databinding.ItemCastBinding
import com.example.appmoive.utils.Constants


class CastAdapter(
    private var castList: List<Cast>,
    private val listener: OnActorClickListener
) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    inner class CastViewHolder(val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun getItemCount(): Int = castList.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castList[position]
        holder.binding.apply {
            tvCastName.text = castMember.name

            Glide.with(root.context)
                .load(Constants.IMAGE_BASE_URL + castMember.profilePath)
                .placeholder(R.drawable.ic_profile2)
                .error(R.drawable.ic_profile2)
                .into(ivCastPhoto)
        }

        //  Thêm sự kiện click
        holder.itemView.setOnClickListener {
            listener.onActorClick(castMember)
        }
    }

    fun setData(newCastList: List<Cast>) {
        this.castList = newCastList
        notifyDataSetChanged()
    }
}