package com.dicoding.recipefy.ui

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.recipefy.data.response.IngredientsResponse
import com.dicoding.recipefy.databinding.ItemIngredientsBinding
import com.dicoding.recipefy.helper.viewBinding

class IncredientsAdapter (
    private val context: Context,
    var data: List<IngredientsResponse.Ingredient>,
    val cellClickListener: CellClickListener
) :
    RecyclerView.Adapter<IncredientsAdapter.MyViewHolder>() {

    var posSelect = 999
    inner class MyViewHolder(binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var tvRecipeName = binding.tvRecipeName
        var imgPhotoRecipe = binding.imgPhotoRecipe
        var rbChecked = binding.rbChecked


        fun bind(data: IngredientsResponse.Ingredient, pos: Int) {
            tvRecipeName.text = data.name_ingredient

            Glide.with(context)
                .load(data.image_url)
                .into(imgPhotoRecipe)

            rbChecked.setOnClickListener {
                if (!rbChecked.isSelected) {
                    rbChecked.isChecked = true
                    rbChecked.isSelected = true
                } else {
                    rbChecked.isChecked = false
                    rbChecked.isSelected = false
                }

                cellClickListener.selectRecipe(data, rbChecked.isSelected)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncredientsAdapter.MyViewHolder {
        return MyViewHolder(parent.viewBinding(ItemIngredientsBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data!![position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    interface CellClickListener {
        fun selectRecipe(data: IngredientsResponse.Ingredient, isChecked: Boolean)
    }
}