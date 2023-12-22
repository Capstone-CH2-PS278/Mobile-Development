package com.dicoding.recipefy.data.response

data class DetailRecipeResponse(
    val recipe: Recipe
) {
    data class Recipe(
        val id: String,
        val name: String,
        val image_url: String,
        val note: String,
        val ingredients: List<String>,
        val tools: List<String>,
        val instructions: List<String>,
        val likes: Int
    )
}