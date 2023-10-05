package com.foodbae.ui.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.foodbae.MainActivity
import com.foodbae.R
import com.foodbae.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : Activity() {
    private lateinit var binding:ActivityLoginBinding

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        binding.buttonLogin.setOnClickListener {
            Toast.makeText(this, "Validating Login...", Toast.LENGTH_SHORT).show()

            if (!validateForm(
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString()
                )
            ) {
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "signInWithEmail:success $binding.editTextEmailLogin")
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }, 4300)
                    }
                }
                .addOnFailureListener {
                    if (auth.currentUser?.email != binding.edtEmail.text.toString()) {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                    return@addOnFailureListener
                }
        }

        actionLoginWithGoogle()

        binding.actionToRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        if (email.isEmpty() || email.isBlank()) {
            binding.edtEmail.requestFocus()
            binding.edtEmail.error = "Please Entered Your Email"

            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString().trim())
                .matches()
        ) {
            binding.edtEmail.error = "Please enter a valid email address"
            return false
        }
        if (password.isEmpty() || password.isBlank()) {
            binding.edtPassword.requestFocus()
            binding.edtPassword.error = "Please Entered Your Password"

            return false
        }
        if (password.length < 8) {
            binding.edtPassword.requestFocus()
            binding.edtPassword.error = "Password too short, enter minimum 8 characters!"

            return false
        }
        return true
    }

    private fun actionLoginWithGoogle(){
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]

        binding.buttonGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }

    }
    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    //updateUI(user)

                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}