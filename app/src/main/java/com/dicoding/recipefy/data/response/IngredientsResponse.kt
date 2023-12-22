package com.dicoding.recipefy.data.response

data class IngredientsResponse(
    val ingredients: List<Ingredient>
) {
    data class Ingredient(
        val id: Int,
        val name_ingredient: String,
        val image_url: String
    )
}