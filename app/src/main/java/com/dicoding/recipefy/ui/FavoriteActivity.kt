package com.dicoding.recipefy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.recipefy.data.response.DetailRecipeResponse
import com.dicoding.recipefy.data.response.FavoriteListResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import com.dicoding.recipefy.databinding.ActivityAllRecipeBinding
import com.dicoding.recipefy.databinding.ActivityDetailRecipeBinding
import com.dicoding.recipefy.databinding.ActivityFavoriteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteBinding

    private var id_user = ""
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id_user = intent.getStringExtra("id_user")?:""
        adapter = FavoriteAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        binding.rvFavorite.adapter = adapter

        getFavorite()

        with(binding) {
            adapter.setOnItemClickListener { clickedRecipe ->
                val userId = intent.getStringExtra("id_user")
                val intent = Intent(this@FavoriteActivity, DetailRecipeActivity::class.java)
                intent.putExtra("id_recipe", clickedRecipe.id)
                intent.putExtra("id_user", userId)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getFavorite()
    }

    private fun getFavorite() {
        Log.d("FavoriteActivity", "userId: $id_user")
        val apiService = ApiConfig.getApiService()
        val call =
            apiService.getFavoritList(id_user)

        call.enqueue(object : Callback<FavoriteListResponse> {
            override fun onResponse(
                call: Call<FavoriteListResponse>,
                response: Response<FavoriteListResponse>
            ) {
                if (response.isSuccessful) {
                    val favoriteList = response.body()
                    if (favoriteList != null) {
                        val listFavorit = favoriteList.favorite
                        adapter.submitList(listFavorit)
                        Log.d("FavoritActivity", "onResponse: Data berhasil diambil")
                    } else {
                        Log.d("FavoritActivity", "onResponse: Tidak ada data favorit")
                    }
                }
            }

            override fun onFailure(call: Call<FavoriteListResponse>, t: Throwable) {
                try {
                    val responseCode = (t as retrofit2.HttpException).code()
                    val errorMessage = t.response()?.errorBody()?.string()
                    Log.e("FavoritActivity", "onFailure: Response Code: $responseCode, Error Message: $errorMessage")
                } catch (e: Exception) {
                    Log.e("FavoritActivity", "onFailure: ${t.message}")
                }
            }

        })
    }
}