package com.dicoding.recipefy.ui

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.recipefy.data.response.FavoriteListResponse
import com.dicoding.recipefy.databinding.ItemFavoriteBinding

class FavoriteAdapter : ListAdapter<FavoriteListResponse.FavoriteItem, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((FavoriteListResponse.FavoriteItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (FavoriteListResponse.FavoriteItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val favorit = getItem(position)
        holder.bind(favorit)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(favorit)
        }
    }

    class MyViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorit: FavoriteListResponse.FavoriteItem) {
            Glide.with(binding.root)
                .load(favorit.image_url)
                .into(binding.imgPhotoRecipe)
            binding.tvRecipeName.text = favorit.name
            binding.tvDescription.text = favorit.note
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteListResponse.FavoriteItem>() {
            override fun areItemsTheSame(oldItem: FavoriteListResponse.FavoriteItem, newItem: FavoriteListResponse.FavoriteItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteListResponse.FavoriteItem, newItem: FavoriteListResponse.FavoriteItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
