package com.dicoding.recipefy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.recipefy.data.response.FilterIngredientsReq
import com.dicoding.recipefy.data.response.RecipeResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import com.dicoding.recipefy.databinding.ActivityRecommendationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationBinding
    private lateinit var viewModel: AllRecipeViewModel
    private lateinit var adapter: RecipesAdapter

    companion object {
        private const val TAG = "Recomendations"
    }

    private var list_ingredients = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list_ingredients = intent.getStringArrayListExtra("list_ingredients")?: mutableListOf()

        viewModel = ViewModelProvider(this).get(AllRecipeViewModel::class.java)
        adapter = RecipesAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecipes.layoutManager = layoutManager
        binding.rvRecipes.adapter = adapter

        getRecipe()

        with(binding) {
            adapter.setOnItemClickListener { clickedRecipe ->
                val userId = intent.getStringExtra("id_user")
//                Toast.makeText(applicationContext, "${clickedRecipe.name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RecommendationActivity, DetailRecipeActivity::class.java)
                intent.putExtra("id_recipe", clickedRecipe.id)
                intent.putExtra("id_user", userId)
                startActivity(intent)
            }
        }
    }

    private fun getRecipe() {
        val filterIngredientsReq = FilterIngredientsReq(
            list_ingredients
        )

        val apiService = ApiConfig.getApiService()
        val call =
            apiService.filterIngredients(filterIngredientsReq)

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
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}