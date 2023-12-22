package com.dicoding.recipefy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.recipefy.data.response.RecipeResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import com.dicoding.recipefy.databinding.ActivitySearchRecipeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchRecipeBinding
    private lateinit var viewModel: AllRecipeViewModel
    private lateinit var adapter: RecipesAdapter

    companion object {
        private const val TAG = "Ingredients"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RecipesAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.rvIngredients.layoutManager = layoutManager
        binding.rvIngredients.adapter = adapter

        viewModel = ViewModelProvider(this).get(AllRecipeViewModel::class.java)

        getRecipe()

        with(binding) {
            adapter.setOnItemClickListener { clickedRecipe ->
                val userId = intent.getStringExtra("id_user")
                val intent = Intent(this@SearchRecipeActivity, DetailRecipeActivity::class.java)
                intent.putExtra("id_recipe", clickedRecipe.id)
                intent.putExtra("id_user", userId)
                startActivity(intent)
            }
        }
    }

    private fun getRecipe() {
        val apiService = ApiConfig.getApiService()
        val call =
            apiService.getAllRecipe("recipes")

        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                Log.d("response", " == " + response.body().toString())
                if (response.isSuccessful) {
                    val recipeResponse = response.body()
                    if (recipeResponse != null) {
                        val initialRecipes = recipeResponse.recipes
                        adapter.setData(initialRecipes)
                    }
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

        binding.searchIngredients.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter.filter.filter(s)

                binding.searchIngredients.setOnClickListener(View.OnClickListener {
                    binding.searchIngredients.isIconified =
                        false
                })

                return false
            }
        })

    }
}