package com.foodbae.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodbae.R
import com.foodbae.databinding.ItemMenuBinding
import com.foodbae.model.MenuModel
import com.foodbae.ui.detail.DetailMenuActivity

class ListMenuAdapter :RecyclerView.Adapter<ListMenuAdapter.ViewHolder>() {
    private var list = ArrayList<MenuModel>()

    fun setData(items: ArrayList<MenuModel>) {
        list.clear()
        list.addAll(items)
        this.notifyDataSetChanged()
    }

    class ViewHolder(private val binding:ItemMenuBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:MenuModel){
            Glide.with(itemView.context)
                .load(data.image)
                .into(binding.itemIvMenu)

            binding.itemNameMenu.text = data.name
            binding.itemSizeMenu.text = itemView.context.getString(R.string.get_size, data.size)
            binding.itemPriceMenu.text = itemView.context.getString(R.string.get_rm, data.price)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailMenuActivity::class.java)
                intent.putExtra(DetailMenuActivity.INTENT_DATA_DETAIL, data)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}