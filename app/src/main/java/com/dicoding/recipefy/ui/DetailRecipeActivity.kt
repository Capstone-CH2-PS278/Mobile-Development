package com.dicoding.recipefy.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.recipefy.R
import com.dicoding.recipefy.data.response.DetailRecipeResponse
import com.dicoding.recipefy.data.response.FavoriteListResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import com.dicoding.recipefy.databinding.ActivityDetailRecipeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private lateinit var viewModel: AllRecipeViewModel

    private var id_recipe = ""
    private var id_user = ""
    private lateinit var adapterIncredients: ListStringAdapter
    private lateinit var adapterTools: ListStringAdapter
    private lateinit var adapterInstructions: ListStringAdapter

    private var favoriteRecipeIds: List<String> = emptyList()

    companion object {
        private const val TAG = "Ingredients"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AllRecipeViewModel::class.java)

        id_user = intent.getStringExtra("id_user") ?: ""
        id_recipe = intent.getStringExtra("id_recipe") ?: ""

        adapterIncredients = ListStringAdapter()
        adapterTools = ListStringAdapter()
        adapterInstructions = ListStringAdapter()

        binding.rvIngredients.layoutManager = LinearLayoutManager(this)
        binding.rvTools.layoutManager = LinearLayoutManager(this)
        binding.rvInstructions.layoutManager = LinearLayoutManager(this)

        binding.rvIngredients.adapter = adapterIncredients
        binding.rvTools.adapter = adapterTools
        binding.rvInstructions.adapter = adapterInstructions

        detailRecipe()

        binding.imgFav.setOnClickListener {
            getUserFavorites(id_user)
            toggleFavorite()
        }

        updateFavoriteImage()
    }

    private fun getUserFavorites(userId: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getFavoritList(userId)

        call.enqueue(object : Callback<FavoriteListResponse> {
            override fun onResponse(
                call: Call<FavoriteListResponse>,
                response: Response<FavoriteListResponse>
            ) {
                if (response.isSuccessful) {
                    val favoriteList = response.body()?.favorite
                    favoriteList?.let {
                        favoriteRecipeIds = it.map { favorite -> favorite.id }
                        // Setelah mendapatkan daftar favorit, perbarui ikon favorit di sini
                        updateFavoriteImage()
                        for (favorite in it) {
                            // Tampilkan data favorit ke logchat
                            Log.d(TAG, "Favorite Recipe: ${favorite.id}")
                            // Tambahan: jika kamu butuh akses ke detail favorit, tambahkan pengolahan di sini
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FavoriteListResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun detailRecipe() {
        val apiService = ApiConfig.getApiService()
        val call =
            apiService.getDetailRecipe(id_recipe)


        call.enqueue(object : Callback<DetailRecipeResponse> {
            override fun onResponse(
                call: Call<DetailRecipeResponse>,
                response: Response<DetailRecipeResponse>
            ) {
                if (response.isSuccessful) {
                    val detailRecipe = response.body()
                    if (detailRecipe != null) {
                        binding.tvNameRecipe.text = detailRecipe.recipe.name

                        Glide.with(this@DetailRecipeActivity)
                            .load(detailRecipe.recipe.image_url)
                            .into(binding.imgPhotoRecipe)

                        val listIncredients = detailRecipe.recipe.ingredients
                        val listTools = detailRecipe.recipe.tools
                        val listInstructions = detailRecipe.recipe.instructions

                        adapterIncredients.submitList(listIncredients)
                        adapterTools.submitList(listTools)
                        adapterInstructions.submitList(listInstructions)

                        getUserFavorites(id_user)

                    }
                }
            }

            override fun onFailure(call: Call<DetailRecipeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun toggleFavorite() {
        Log.d(TAG, "userId: $id_user, recipeId: $id_recipe")

        viewModel.toggleFavorite(id_user, id_recipe).observe(this, Observer { response ->
            Log.d(TAG, "API Response: $response")
            if (response != null) {
                if (response.success) {
                    if (response.message == "Recipe added to favorites") {
                        Toast.makeText(this, "Favorit disimpan", Toast.LENGTH_SHORT).show()
                    } else if (response.message == "Recipe removed from favorites") {
                        Toast.makeText(this, "Favorit dihapus", Toast.LENGTH_SHORT).show()
                    }
                    getUserFavorites(id_user)
                    updateFavoriteImage()
                } else {
                    Toast.makeText(
                        this,
                        "Gagal menyimpan favorit: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun updateFavoriteImage() {
        val isFavoriteRecipe = favoriteRecipeIds.contains(id_recipe)
        if (isFavoriteRecipe) {
            binding.imgFav.setImageResource(R.drawable.favoriteicon)
        } else {
            binding.imgFav.setImageResource(R.drawable.baseline_favorite_24)
        }
    }
}