package com.dicoding.recipefy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.recipefy.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java) // Inisialisasi SignUpViewModel

        binding.tvGoLogin.setOnClickListener {
            val loginIntent = Intent(this@SignUp, Login::class.java)
            startActivity(loginIntent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPass.text.toString()

            if (email.isNotEmpty() && password.length >= 6) {
                val requestBody = mapOf(
                    "email" to email,
                    "username" to username,
                    "password" to password
                )

                // Debugging: Print JSON payload before sending to the server
                val jsonPayload = Gson().toJson(requestBody)
                Log.d("LoginActivity", "JSON Payload: $jsonPayload")
                signUpViewModel.signUp(this, requestBody)
            } else {
                if (password.length < 6) {
                    Toast.makeText(this, "Minimum password length is 6 characters", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Data cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
        signUpViewModel.signUpResult.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()
                val loginIntent = Intent(this@SignUp, Login::class.java)
                startActivity(loginIntent)
            }
        })
    }
}