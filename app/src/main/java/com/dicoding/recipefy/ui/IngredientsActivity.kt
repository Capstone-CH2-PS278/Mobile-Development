package com.dicoding.recipefy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.recipefy.data.response.IngredientsResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import com.dicoding.recipefy.databinding.ActivityIngredientsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientsActivity : AppCompatActivity(), IncredientsAdapter.CellClickListener {
    private lateinit var binding : ActivityIngredientsBinding
    private lateinit var ingredientsAdapter: IncredientsAdapter

    val TAG = "Ingredients"

    private var listSelectIngredients = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIngredients()

        binding.imgNext.setOnClickListener {
            if (listSelectIngredients.isEmpty()){
                Toast.makeText(this, "Silahkan pilih resep terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val userId = intent.getStringExtra("id_user")
                val intent = Intent(this@IngredientsActivity, RecommendationActivity::class.java)
                intent.putStringArrayListExtra("list_ingredients", listSelectIngredients as ArrayList<String>)
                intent.putExtra("id_user", userId)
                startActivity(intent)
            }
        }
    }

    private fun getIngredients(){
        val apiService = ApiConfig.getApiService()
        val call =
            apiService.getIngredients()

        call.enqueue(object : Callback<IngredientsResponse> {
            override fun onResponse(
                call: Call<IngredientsResponse>,
                response: Response<IngredientsResponse>
            ) {
                if (response.isSuccessful) {
                    val recipeResponse = response.body()
                    if (recipeResponse != null ) {
                        val data = recipeResponse.ingredients

                        ingredientsAdapter = IncredientsAdapter(this@IngredientsActivity, data, this@IngredientsActivity)

                        binding.rvIngredients.layoutManager = LinearLayoutManager(this@IngredientsActivity)
                        binding.rvIngredients.adapter = ingredientsAdapter
                    }
                }
            }

            override fun onFailure(call: Call<IngredientsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    override fun selectRecipe(data: IngredientsResponse.Ingredient, isChecked: Boolean) {
        if (isChecked){
            if (listSelectIngredients.contains(data.name_ingredient)){
            } else {
                listSelectIngredients.add(data.name_ingredient)
            }
        } else {
            listSelectIngredients.remove(data.name_ingredient)
        }

//        Toast.makeText(this, "listSelectIngredients " + listSelectIngredients, Toast.LENGTH_SHORT).show()
    }
}