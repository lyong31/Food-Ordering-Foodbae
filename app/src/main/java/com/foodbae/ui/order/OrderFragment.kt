package com.foodbae.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodbae.MainActivity
import com.foodbae.R
import com.foodbae.adapter.OrderAdapter
import com.foodbae.databinding.FragmentOrderBinding
import com.foodbae.model.CartModel
import com.foodbae.model.OrderModel
import com.foodbae.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var cartReference: DatabaseReference

    private lateinit var orderAdapter: OrderAdapter
    private var listCartFirebase: ArrayList<OrderModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        auth = Firebase.auth

        orderAdapter = OrderAdapter()

        loadCart()
        setRecyclerView()
    }

    private fun setToolbar(){
        binding.layoutToolbar.Title.text = getString(R.string.title_toolbar_order)
        (activity as MainActivity).setSupportActionBar(binding.layoutToolbar.toolbar)
        (activity as MainActivity).supportActionBar.apply {
            this?.setDisplayShowHomeEnabled(false)
            this?.setDisplayHomeAsUpEnabled(false)
            this?.setDisplayShowTitleEnabled(false)
        }
    }
    private fun loadCart() {
        //Get data from firebase
        listCartFirebase.clear()
        cartReference = Firebase.database.reference.child("Orders").child("user-order").child(auth.uid.toString())
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val cart = datasnap.getValue<OrderModel>()
                        if (cart != null) {
                            listCartFirebase.add(cart)
                        }
                    }
                    orderAdapter.setData(listCartFirebase)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("On Cancelled Load Data", "Data Error", error.toException())
                Toast.makeText(
                    requireContext(),
                    "Data Error : " + error.toException(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        cartReference.addValueEventListener(dataListener)

    }

    private fun setRecyclerView() {
        binding.rvOrder.setHasFixedSize(true)
        binding.rvOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrder.adapter = orderAdapter
    }

}