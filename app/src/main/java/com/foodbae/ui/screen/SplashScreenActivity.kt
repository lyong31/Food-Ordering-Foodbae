package com.foodbae.ui.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.foodbae.MainActivity
import com.foodbae.databinding.ActivitySplashScreenBinding
import com.foodbae.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashScreenBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.buttonStarted.setOnClickListener {
            checkUser()
        }
    }

    private fun checkUser(){
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(
                this,
                "Welcome to Foodbae",
                Toast.LENGTH_SHORT
            )
                .show()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}