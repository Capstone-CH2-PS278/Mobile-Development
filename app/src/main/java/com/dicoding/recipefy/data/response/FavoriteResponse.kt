package com.dicoding.recipefy.data.response

import com.google.gson.annotations.SerializedName

data class FavoriteRequest(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("recipeId")
    val recipeId: String
)

data class FavoriteResponse(
    @SerializedName("status")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,
)