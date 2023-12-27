package com.dicoding.recipefy.data.response

import com.google.gson.annotations.SerializedName

data class RecipeResponse(

	@field:SerializedName("recipes")
	val recipes: List<RecipesItem>
)

data class RecipesItem(

	@field:SerializedName("note")
	val note: String,

	@field:SerializedName("instructions")
	val instructions: List<String>,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("ingredients")
	val ingredients: List<String>,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("tools")
	val tools: List<String>,

	@field:SerializedName("updateTime")
	val updateTime: Int
)