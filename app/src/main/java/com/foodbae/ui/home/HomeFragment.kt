package com.foodbae.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.foodbae.adapter.HomeAdapter
import com.foodbae.databinding.FragmentHomeBinding
import com.foodbae.model.ResponseMenu
import com.foodbae.utils.readJSONFile
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeAdapter = HomeAdapter()

        parsingJsonMenu()
        setRecyclerViewDrink()
    }
    private fun parsingJsonMenu() {
        val jsonFileString = readJSONFile(requireContext(), "menu.json")

        val gson = Gson()
        val dataMenu = gson.fromJson(jsonFileString.toString(), ResponseMenu::class.java)

        homeAdapter.setData(dataMenu.data)

    }

    private fun setRecyclerViewDrink() {
        binding.rvFeeling.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            adapter = homeAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}