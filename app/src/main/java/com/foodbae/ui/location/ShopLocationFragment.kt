package com.foodbae.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foodbae.databinding.FragmentShopLocationBinding

class ShopLocationFragment : Fragment() {
    private var _binding: FragmentShopLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopLocationBinding.inflate(inflater, container, false)
        return binding.root
    }


}