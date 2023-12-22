package com.dicoding.recipefy.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.recipefy.R
import com.dicoding.recipefy.data.response.RecipeResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import com.dicoding.recipefy.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var ivAllRecipe: ImageView
    private lateinit var ivIngredients: ImageView
    private lateinit var ivFavorite: ImageView
    private lateinit var searchBar: com.google.android.material.search.SearchBar
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi adapter
        adapter = RecipesAdapter()

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.rvRecommend.layoutManager = layoutManager
        binding.rvRecommend.adapter = adapter

        // Inisialisasi view
        ivAllRecipe = binding.ivAllrecipe
        ivIngredients = binding.ivIngredients
        searchBar = binding.searchBar
        ivFavorite = binding.ivFavorite

        // Set up click listeners
        ivAllRecipe.setOnClickListener {
            val userId = intent.getStringExtra("id_user")
            val allRecipeIntent = Intent(this@MainActivity, AllRecipe::class.java)
            allRecipeIntent.putExtra("id_user", userId)
            startActivity(allRecipeIntent)
        }

        searchBar.setOnClickListener {
            val userId = intent.getStringExtra("id_user")
            val intent = Intent(this@MainActivity, SearchRecipeActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
        }

        ivIngredients.setOnClickListener {
            val userId = intent.getStringExtra("id_user")
            val intent = Intent(this@MainActivity, IngredientsActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
        }

        ivFavorite.setOnClickListener {
            val userId = intent.getStringExtra("id_user")
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
        }

        // Fetch recipes
        getRecipe()

        // Set up item click listener
        with(binding) {
            adapter.setOnItemClickListener { clickedRecipe ->
                val userId = intent.getStringExtra("id_user")
                val intent = Intent(this@MainActivity, DetailRecipeActivity::class.java)
                intent.putExtra("id_recipe", clickedRecipe.id)
                intent.putExtra("id_user", userId)
                startActivity(intent)
            }
        }
    }

    private fun getRecipe() {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllRecipe("recipes")

        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                if (response.isSuccessful) {
                    val recipeResponse = response.body()
                    if (recipeResponse != null) {
                        val initialRecipes = recipeResponse.recipes
                        adapter.submitList(initialRecipes)
                    }
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        showLogoutConfirmationDialog()
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Konfirmasi Logout")
        alertDialogBuilder.setMessage("Apakah Anda yakin akan logout?")
        alertDialogBuilder.setPositiveButton("Ya") { _, _ ->
            navigateToLogin()
        }
        alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun navigateToLogin() {
        val loginIntent = Intent(this@MainActivity, Login::class.java)
        startActivity(loginIntent)
        finish()
    }
}