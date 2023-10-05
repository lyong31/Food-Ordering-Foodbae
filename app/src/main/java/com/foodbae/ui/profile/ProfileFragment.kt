package com.foodbae.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.foodbae.databinding.FragmentProfileBinding
import com.foodbae.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var userID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        userID = auth.uid.toString()

        database = Firebase.database
        databaseReference = database.reference.child("Users").child(userID)

        getDataUser()
        getDataGoogle()
    }

    private fun getDataGoogle() {
        val firebaseUser = auth.currentUser
        val email = firebaseUser?.email
        val contact = firebaseUser?.phoneNumber

        binding.tvEmail.text = email
        binding.tvContact.text = contact
    }

    private fun getDataUser() {
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val infoUser = snapshot.getValue<UserModel>()
                if (infoUser != null) {
                    binding.tvEmail.text = infoUser.email
                    binding.tvContact.text = infoUser.contact
                    binding.tvPostalCode.text = infoUser.postal_code
                    binding.tvState.text = infoUser.state
                    binding.tvAddress.text = infoUser.address
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

        databaseReference.addValueEventListener(dataListener)
    }

}