package com.foodbae.ui.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.foodbae.R
import com.foodbae.adapter.ListMenuAdapter
import com.foodbae.databinding.ActivityMenuBinding
import com.foodbae.model.FoodbaeModel

class MenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMenuBinding

    private lateinit var menuAdapter: ListMenuAdapter

    private var data: FoodbaeModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuAdapter = ListMenuAdapter()

        data = intent.getParcelableExtra(EXTRA_MENU)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rvMenu.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MenuActivity, 2)

            menuAdapter.setData(data!!.menu_list)

            adapter = menuAdapter
        }
    }

    companion object {
        const val EXTRA_MENU = "extra_menu"
    }
}