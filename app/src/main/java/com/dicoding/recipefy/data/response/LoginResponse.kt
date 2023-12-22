package com.dicoding.recipefy.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("userId")
    val userId: String?
)