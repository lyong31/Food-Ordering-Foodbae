package com.foodbae.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foodbae.R
import com.foodbae.databinding.ItemOrderBinding
import com.foodbae.model.OrderModel

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private var listOrder = ArrayList<OrderModel>()

    fun setData(items: ArrayList<OrderModel>) {
        listOrder.clear()
        listOrder.addAll(items)
        this.notifyDataSetChanged()
    }

    class ViewHolder(private var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrderModel) {
            binding.itemOrderMenu.text =
                itemView.context.getString(R.string.get_food, data.order_menu)
            binding.itemOrderQuantity.text =
                itemView.context.getString(R.string.get_quantity, data.order_quantity)
            binding.itemOrderPrice.text =
                itemView.context.getString(R.string.get_total_price, data.order_price)
            binding.itemOrderDate.text =
                itemView.context.getString(R.string.get_date, data.order_date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOrder[position])
    }

    override fun getItemCount(): Int = listOrder.size
}