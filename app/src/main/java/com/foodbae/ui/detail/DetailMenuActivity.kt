package com.foodbae.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.foodbae.R
import com.foodbae.databinding.ActivityDetailMenuBinding
import com.foodbae.model.CartModel
import com.foodbae.model.MenuModel
import com.foodbae.ui.cart.CartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DetailMenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailMenuBinding

    private lateinit var data:MenuModel

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()

        data = intent.getParcelableExtra(INTENT_DATA_DETAIL)!!
        getData(data)

        auth = Firebase.auth
        databaseReference = Firebase.database.reference

        binding.buttonCart.setOnClickListener {
            setAddToCart()
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun setToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar.apply {
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun getData(data: MenuModel){
        if(data.id!!.subSequence(0,5) == "foods"){
            Glide.with(this)
                .load(data.image)
                .into(binding.ivMenu)

            binding.tvTitleProduct.text = data.name
            binding.tvPriceMenu.visibility = View.GONE
            binding.tvSizeMenu.visibility = View.GONE
            binding.tvDescriptionMenu.text = data.description
        }else if(data.id!!.subSequence(0,6)=="drinks"){
            Glide.with(this)
                .load(data.image)
                .into(binding.ivMenu)

            binding.tvTitleProduct.text = data.name
            binding.tvPriceMenu.text = getString(R.string.get_rm,data.price)
            binding.tvSizeMenu.text = getString(R.string.get_size,data.size)
            binding.tvDescriptionMenu.text = data.description
        }
    }

    private fun setAddToCart(){
        val userId = auth.currentUser?.uid
        if(userId != null){
            addToCart(userId,data)
        }
    }
    private fun addToCart(userID:String,data: MenuModel){
        val key = databaseReference.child("Cart").push().key
        if(key == null){
            Log.w("label", "Couldn't get push key for post")
            return
        }

        val cart = CartModel(key, data.id ,data.name,data.price,data.image,data.size ,1)
        Log.d("DATA ADD CART",cart.toString())
        val cartValue = cart.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/user-cart/$userID/$key" to cartValue
        )
        databaseReference.child("Cart").updateChildren(childUpdates)
            .addOnSuccessListener {
                Toast.makeText(this, "Added To Cart Success", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Added Failed", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val INTENT_DATA_DETAIL = "intent_data_detail"
    }
}