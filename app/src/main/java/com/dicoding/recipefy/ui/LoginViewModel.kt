// LoginViewModel.kt
package com.dicoding.recipefy.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.recipefy.data.response.LoginResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean> = _login

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val apiService = ApiConfig.getApiService()

    fun login(token: String) {
        val requestBody = mapOf("idToken" to token)
        apiService.login(requestBody).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse?.userId != null) {
                        _userId.value = userResponse.userId
                        _login.value = true // Menandakan bahwa login berhasil
                        Log.d("LoginViewModel", "UserId: ${userResponse.userId}")
                    } else {
                        Log.e("LoginViewModel", "Failed to get user information")
                    }
                } else {
                    Log.e("LoginViewModel", "Failed to get user information")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.value = "Error: ${t.message}"
            }
        })
    }
}
