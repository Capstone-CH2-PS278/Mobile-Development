package com.dicoding.recipefy.data.retrofit

import com.dicoding.recipefy.data.response.DetailRecipeResponse
import com.dicoding.recipefy.data.response.FavoriteListResponse
import com.dicoding.recipefy.data.response.FavoriteRequest
import com.dicoding.recipefy.data.response.FavoriteResponse
import com.dicoding.recipefy.data.response.FilterIngredientsReq
import com.dicoding.recipefy.data.response.IngredientsResponse
import com.dicoding.recipefy.data.response.LoginResponse
import com.dicoding.recipefy.data.response.RecipeResponse
import com.dicoding.recipefy.data.response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import retrofit2.http.Query

interface ApiService {
    @POST("signup")
    fun createAccount(
        @Body requestBody: Map<String, String>
    ): Call<SignUpResponse>

    @POST("login")
    fun login(@Body requestBody: Map<String, String>
    ): Call<LoginResponse>

    @GET("recipes")
    fun getAllRecipe(
        @Query("recipes") recipes: String
    ): Call<RecipeResponse>

    @GET("recipes/{id_recipe}")
    fun getDetailRecipe(
        @Path("id_recipe") id_recipe: String
    ): Call<DetailRecipeResponse>

    @GET("ingredients")
    fun getIngredients(): Call<IngredientsResponse>

    @POST("filterIngredient")
    fun filterIngredients(@Body requestBody: FilterIngredientsReq
    ): Call<RecipeResponse>

    @POST("favorite")
    fun toggleFavorite(@Body requestBody: FavoriteRequest): Call<FavoriteResponse>

    @GET("favorite/{userId}")
    fun getFavoritList(
        @Path("userId") userId: String
    ): Call<FavoriteListResponse>

}