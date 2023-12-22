package com.dicoding.recipefy.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.recipefy.data.response.SignUpResponse
import com.dicoding.recipefy.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel(){

    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: LiveData<Boolean> = _signUpResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val apiService = ApiConfig.getApiService()

    companion object {
        private const val TAG = "SignUpViewModel"
    }

    fun signUp(context: Context, requestBody: Map<String, String>) {
        val call = apiService.createAccount(requestBody)

        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    _signUpResult.value = true
                } else {
                    if (response.code() == 400) {
                        _signUpResult.value = false
                        _error.value = "Email is already"
                        Toast.makeText(context, "Email is already", Toast.LENGTH_SHORT).show()
                    } else {
                        // Respons lain yang tidak diharapkan
                        _signUpResult.value = false
                        _error.value = "Registration Failed: ${response.message()}"
                        Log.d(TAG, "Response unsuccessful: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                _signUpResult.value = false
                _error.value = "Error: ${t.message}"
                Log.d(TAG, "Failure: ${t.message}")
            }
        })
    }
}