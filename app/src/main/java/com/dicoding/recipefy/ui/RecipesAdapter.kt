package com.dicoding.recipefy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.recipefy.data.response.RecipesItem
import com.dicoding.recipefy.databinding.ItemRecipesBinding

class RecipesAdapter : ListAdapter<RecipesItem, RecipesAdapter.MyViewHolder>(DIFF_CALLBACK),
    Filterable {

    private var list = mutableListOf<RecipesItem>()

    private var onItemClickListener: ((RecipesItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (RecipesItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(recipe)
        }
    }

    class MyViewHolder(private val binding: ItemRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipesItem) {
            Glide.with(binding.root)
                .load(recipe.imageUrl)
                .into(binding.imgPhotoRecipe)
            binding.tvRecipeName.text = recipe.name
            binding.tvDescription.text = recipe.note
        }
    }

    fun setData(list: List<RecipesItem>){
        this.list = list as MutableList<RecipesItem>
        submitList(list)
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

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<RecipesItem>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list)
            } else {
                for (item in list) {
                    if (item.name.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<RecipesItem>)
        }

    }
}
