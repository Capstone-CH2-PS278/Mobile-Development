package com.dicoding.recipefy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.recipefy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.gson.Gson

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.tvGoSignUp.setOnClickListener {
            val signUpIntent = Intent(this@Login, SignUp::class.java)
            startActivity(signUpIntent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val token = tokenTask.result?.token
                                token?.let {
                                    val requestBody = mapOf("idToken" to it)

                                    // Debugging: Print JSON payload before sending to the server
                                    val jsonPayload = Gson().toJson(requestBody)
                                    Log.d("LoginActivity", "JSON Payload: $jsonPayload")

                                    viewModel.login(it)
                                }
                            } else {
                                Toast.makeText(this@Login, "Failed to retrieve token", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Data cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.login.observe(this, Observer { login ->
            if (login) {
                Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_LONG).show()
                val mainActivityIntent = Intent(this@Login, MainActivity::class.java)

                // Tambahkan data userId ke Intent
                val userId = firebaseAuth.currentUser?.uid
                userId?.let {
                    mainActivityIntent.putExtra("id_user", it)
                    startActivity(mainActivityIntent)
                    finish()
                } ?: run {
                    Toast.makeText(this@Login, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}