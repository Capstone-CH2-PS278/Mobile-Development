package com.dicoding.recipefy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.recipefy.data.response.FavoriteListResponse
import com.dicoding.recipefy.data.response.RecipesItem
import com.dicoding.recipefy.databinding.ItemFavoriteBinding
import com.dicoding.recipefy.databinding.ItemRecommendBinding

class RecommendAdapter : ListAdapter<RecipesItem, RecommendAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((RecipesItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (RecipesItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recommend = getItem(position)
        holder.bind(recommend)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(recommend)
        }
    }

    class MyViewHolder(private val binding: ItemRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recommend: RecipesItem) {
            Glide.with(binding.root)
                .load(recommend.imageUrl)
                .into(binding.imgPhotoRecipe)
            binding.tvRecipeName.text = recommend.name
            binding.tvDescription.text = recommend.note
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipesItem>() {
            override fun areItemsTheSame(oldItem: RecipesItem, newItem: RecipesItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecipesItem, newItem: RecipesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
