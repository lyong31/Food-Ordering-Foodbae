package com.foodbae.ui.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodbae.adapter.TypeMenuAdapter
import com.foodbae.databinding.ActivityMenuTypeBinding
import com.foodbae.model.FeelingModel

class MenuTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuTypeBinding

    private lateinit var typeAdapter: TypeMenuAdapter

    private var menuModel: FeelingModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        typeAdapter = TypeMenuAdapter()

        menuModel = intent?.getParcelableExtra(EXTRA_FEELING)
        menuModel?.results?.let { typeAdapter.setData(it) }

        setRecyclerViewDrink()
    }

    private fun setRecyclerViewDrink() {

        binding.rvTypeMenu.apply {
            layoutManager = LinearLayoutManager(this@MenuTypeActivity)
            setHasFixedSize(true)
            adapter = typeAdapter
        }
    }

    companion object {
        const val EXTRA_FEELING = "extra_feeling"
    }
}