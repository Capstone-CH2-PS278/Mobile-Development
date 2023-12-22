package com.dicoding.recipefy.data.response

data class FavoriteListResponse(
    val favorite: List<FavoriteItem>,
) {

    data class FavoriteItem(
        val id: String,
        val name: String,
        val image_url: String,
        val note: String,
    )
}