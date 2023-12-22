package com.dicoding.recipefy.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.recipefy.data.response.FavoriteRequest
import com.dicoding.recipefy.data.response.FavoriteResponse
import com.dicoding.recipefy.data.response.RecipeResponse
import com.dicoding.recipefy.data.response.RecipesItem
import com.dicoding.recipefy.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllRecipeViewModel : ViewModel() {

    private val _allRecipe = MutableLiveData<List<RecipesItem>>()
    val allRecipe: LiveData<List<RecipesItem>> = _allRecipe

    private val _toogleFavorite = MutableLiveData<List<RecipesItem>>()
    val  toogleFavorite : LiveData<List<RecipesItem>> =  _toogleFavorite

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    private val apiService = ApiConfig.getApiService()


    companion object {
        private const val TAG = "AllRecipeViewModel"
    }

    init {
        showAllRecipe(recipes = "recipes")
    }

    fun showAllRecipe(recipes: String) {
        val client = ApiConfig.getApiService().getAllRecipe(recipes)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                if (response.isSuccessful) {
                    _allRecipe.value = response.body()?.recipes
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun toggleFavorite(userId: String, recipeId: String): LiveData<FavoriteResponse> {
        val result = MutableLiveData<FavoriteResponse>()

        if (userId.isNotBlank() && recipeId.isNotBlank()) {
            val requestBody = FavoriteRequest(userId, recipeId)
            val call = apiService.toggleFavorite(requestBody)

            call.enqueue(object : Callback<FavoriteResponse> {
                override fun onResponse(
                    call: Call<FavoriteResponse>,
                    response: Response<FavoriteResponse>
                ) {
                    if (response.isSuccessful) {
                        val favoriteResponse = response.body()
                        result.value = favoriteResponse
                    } else {
                        Log.e(TAG, "Failed to toggle favorite: ${response.message()}")
                        result.value = FavoriteResponse(false, "Failed to toggle favorite")
                    }
                }

                override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                    Log.e(TAG, "Network error: ${t.message}")
                    result.value = FavoriteResponse(false, "Network error")
                }
            })
        } else {
            Log.e(TAG, "userId atau recipeId kosong")
            result.value = FavoriteResponse(false, "userId atau recipeId kosong")
        }

        return result
    }
}