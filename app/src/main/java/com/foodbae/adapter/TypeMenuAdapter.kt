package com.foodbae.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foodbae.databinding.ItemTypeMenuBinding
import com.foodbae.model.FoodbaeModel
import com.foodbae.ui.menu.MenuActivity

class TypeMenuAdapter : RecyclerView.Adapter<TypeMenuAdapter.ViewHolder>() {
    private var listTypeMenu = ArrayList<FoodbaeModel>()

    fun setData(items: ArrayList<FoodbaeModel>) {
        listTypeMenu.clear()
        listTypeMenu.addAll(items)
        this.notifyDataSetChanged()
    }

    class ViewHolder(private var binding: ItemTypeMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FoodbaeModel) {
            binding.nameTypeMenu.text = data.type_menu

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MenuActivity::class.java)
                intent.putExtra(MenuActivity.EXTRA_MENU,data)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTypeMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTypeMenu[position])
    }

    override fun getItemCount(): Int = listTypeMenu.size
}