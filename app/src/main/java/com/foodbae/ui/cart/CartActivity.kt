package com.foodbae.ui.cart

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodbae.MainActivity
import com.foodbae.R
import com.foodbae.adapter.CartAdapter
import com.foodbae.databinding.ActivityCartBinding
import com.foodbae.helper.DateHelper
import com.foodbae.model.CartModel
import com.foodbae.model.OrderModel
import com.foodbae.model.UserModel
import com.foodbae.ui.maps.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class CartActivity : AppCompatActivity(), CartAdapter.ItemCartCallback {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var cartReference: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var orderReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private var listCartFirebase: ArrayList<CartModel> = arrayListOf()

    private var totalPrice = 0
    private var totalQuantity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        auth = Firebase.auth

        cartReference = Firebase.database.reference

        cartAdapter = CartAdapter(this)

        getDataUser()

        loadCart()

        buttonAction()
        setRecyclerView()
    }

    private fun setToolbar() {
        binding.layoutToolbar.Title.text = getString(R.string.title_activity_cart)

        setSupportActionBar(binding.layoutToolbar.toolbar)
        supportActionBar.apply {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayShowHomeEnabled(true)
        }
    }

     private fun getDataUser() {
         database = Firebase.database
         databaseReference = database.reference.child("Users").child(auth.uid.toString())

         val dataListener = object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 val infoUser = snapshot.getValue<UserModel>()
                 if (infoUser != null) {
                     binding.tvAddress.text = infoUser.address
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 Log.w("On Cancelled Load Data", "Data Error", error.toException())
                 Toast.makeText(
                     this@CartActivity,
                     "Data Error : " + error.toException(),
                     Toast.LENGTH_SHORT
                 ).show()
             }
         }
         databaseReference.addValueEventListener(dataListener)
     }

    private fun loadCart() {
        //Get data from firebase
        listCartFirebase.clear()
        val query: Query
        query = cartReference.child("Cart").child("user-cart").child(auth.uid.toString())
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val cart = datasnap.getValue<CartModel>()
                        if (cart != null) {
                            listCartFirebase.add(cart)
                            setConfirmOrder(data = cart)
                        }
                    }
                    cartAdapter.setData(listCartFirebase)

                    binding.headerCountOrder.text =
                        getString(R.string.get_order_cart, cartAdapter.itemCount.toString())

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity,"Load Data Cart Failed",Toast.LENGTH_LONG).show()
            }
        })
        listCartFirebase.clear()
    }

    private fun buttonAction() {
        binding.cardLocation.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun setConfirmOrder(data: CartModel) {
        orderReference = Firebase.database.reference

        val key = orderReference.child("Orders").push().key
        if(key == null){
            Log.w("label", "Couldn't get push key for post")
            return
        }

        val date = DateHelper.getCurrentDate()
        binding.buttonOrder.setOnClickListener {

            val order = OrderModel(data.menu_name, totalQuantity, totalPrice,date)

            val user = auth.currentUser?.uid

            val orderValue = order.toMap()

            val childUpdates = hashMapOf<String, Any>(
                "/user-order/$user/$key" to orderValue
            )
            orderReference.child("Orders").updateChildren(childUpdates)
                .addOnSuccessListener {
                    binding.layoutMessageOrderConfirm.layoutConfirmOrder.visibility =
                        View.VISIBLE
                    binding.cardLocation.visibility = View.GONE

                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar.visibility = View.VISIBLE
                        startActivity(Intent(this, MainActivity::class.java))
                    }, 5000)

                    //If Confirm Order Success, List In Cart Deleted
                    cartReference =FirebaseDatabase.getInstance().getReference("Cart").child("user-cart").child(auth.uid.toString())
                    cartReference.removeValue().addOnSuccessListener {
                        Log.d("FIREBASE CART DELETE","Success Deleted List Cart")
                    }.addOnFailureListener {
                        Log.e("FIREBASE CART DELETE", "On Failure Deleted List Cart")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Added Failed", Toast.LENGTH_SHORT).show()
                    return@addOnFailureListener
                }
        }
    }

    private fun setRecyclerView() {
        binding.rvCart.setHasFixedSize(true)
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter
    }

    override fun onTotalQuantity(price: Int, quantity: Int) {
        totalQuantity = quantity
        totalPrice = price
        Log.d("TOTAL PRICE IN CART",price.toString())
        Log.d("TOTAL QUANTITY IN CART",quantity.toString())

    }

    override fun onAddItems(model: CartModel, position: Int) {

       /* cartAdapter.notifyItemChanged(position)
        Log.d("TOTAL PRICE",totalPrice.toString())
        Log.d("TOTAL QUANTITY",model.quantity.toString())*/
    }

    override fun onMinusItems(model: CartModel, position: Int) {
        /*model.quantity.minus(1)
        totalPrice -= 1
        Log.d("TOTAL PRICE",totalPrice.toString())
        Log.d("TOTAL QUANTITY",model.quantity.toString())
        cartAdapter.notifyItemChanged(position)*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}