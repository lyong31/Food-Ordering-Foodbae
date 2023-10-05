package com.foodbae.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.foodbae.MainActivity
import com.foodbae.databinding.ActivityRegisterBinding
import com.foodbae.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.reference

        binding.progressBar2.visibility = View.GONE

        binding.buttonSignup.setOnClickListener {
            Toast.makeText(this,"Validating Data...", Toast.LENGTH_SHORT).show()

            registerUser(
                binding.edtUsername.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString(),
                binding.edtContact.text.toString(),
                binding.edtPostalCode.text.toString(),
                binding.edtState.text.toString(),
                binding.edtAddress.text.toString()
            )
        }
    }

    private fun registerUser(userName:String, email:String, password:String, phone:String, postal_code:String,state:String,address:String) {

        if (!validateForm(userName, email, password, phone, postal_code,state,address)) {
            return
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("On Success Register", "createUserWithEmail:success")

                        val userData = UserModel(userName,email,password,phone,postal_code,state,address)

                        val user = auth.currentUser?.uid

                        Log.d("CHECK UI USER",user.toString())

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user!!)
                        databaseReference.setValue(userData)
                            .addOnCompleteListener {
                                if (task.isSuccessful) {
                                    binding.progressBar2.visibility = View.VISIBLE
                                    binding.layoutFormRegister.alpha = 0.6F
                                    binding.layoutFormRegister.isClickable = false

                                    Handler(Looper.getMainLooper()).postDelayed({
                                        startActivity(Intent(this, MainActivity::class.java))

                                        binding.edtUsername.setText("")
                                        binding.edtEmail.setText("")
                                        binding.edtPassword.setText("")
                                        binding.edtContact.setText("")
                                        binding.edtPostalCode.setText("")
                                        binding.edtState.setText("")
                                        binding.edtAddress.setText("")
                                    }, 3500)
                                }
                            }

                    } else {
                        binding.progressBar2.visibility = View.GONE
                        binding.layoutFormRegister.isClickable = true
                        // If sign in fails, display a message to the user.
                        Log.e("On Failure Register", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this,
                            "The email address is already in use by another account.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(
        userName: String,
        email: String,
        password: String,
        phone: String,
        postal_code:String,
        state: String,
        address:String
    ): Boolean {
        if (userName.isEmpty() || userName.isBlank()) {
            binding.edtUsername.requestFocus()
            binding.edtUsername.error = "Please Entered Your Username"
            return false
        }
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
        if(phone.isEmpty() || phone.isBlank()){
            binding.edtContact.requestFocus()
            binding.edtContact.error = "Please Entered Your Phone"
        }
        if(!Patterns.PHONE.matcher(binding.edtContact.text!!.trim()).matches()){
            binding.edtContact.requestFocus()
            binding.edtContact.error = "Please Entered Valid Your Phone"
        }
        if(state.isEmpty() || state.isBlank()){
            binding.edtState.requestFocus()
            binding.edtState.error = "Please Entered Your State"
        }
        if(postal_code.isEmpty() || postal_code.isBlank()){
            binding.edtPostalCode.requestFocus()
            binding.edtPostalCode.error = "Please Entered Your Postal Code"
        }
        if(address.isEmpty() || address.isBlank()){
            binding.edtAddress.requestFocus()
            binding.edtAddress.error = "Please Entered Your Address"
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
}