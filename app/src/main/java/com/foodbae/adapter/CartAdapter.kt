package com.foodbae.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodbae.R
import com.foodbae.databinding.ItemCartBinding
import com.foodbae.model.CartModel

class CartAdapter(
    private val itemCartCallback: ItemCartCallback
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var listCart = ArrayList<CartModel>()
    private var qtyItem: Int = 0
    private var totalPrice : Int = 0

    private var totalCountPrice : Int = 0
    private var totalCountQuantity: Int = 0

    fun setData(items: ArrayList<CartModel>) {
        listCart.clear()
        listCart.addAll(items)
        this.notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CartModel) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(binding.ivCoverProduct)

            binding.tvTitleProduct.text = data.menu_name
            binding.tvPrice.text = itemView.context.getString(R.string.get_rm, data.menu_price)
            binding.etQty.setText((data.quantity).toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCart[position])

        val data = listCart[position]

        totalCountPrice += data.menu_price
        data.total_price = totalCountPrice

        totalCountQuantity += data.quantity
        data.quantity = totalCountQuantity
        Log.d("Total PRICE",  data.total_price.toString())

        Log.d("Total Quantity",  data.quantity.toString())

        holder.binding.btnAddItem.setOnClickListener {
              qtyItem = data.quantity
              qtyItem ++
              holder.binding.etQty.setText(qtyItem.toString())
              Log.d("CHECK QUANTITY",qtyItem.toString())
              changeQty(holder,data)
        }
        holder.binding.btnReduceItem.setOnClickListener {
            if(qtyItem > 1){
                qtyItem --
                holder.binding.etQty.setText(qtyItem.toString())
                Log.d("CHECK QUANTITY MINUS",qtyItem.toString())
                changeQty(holder,data)
            }
        }
    }

    override fun getItemCount(): Int = listCart.size

    interface ItemCartCallback {
        fun onTotalQuantity(price:Int,quantity:Int)
        fun onAddItems(model: CartModel, position: Int)
        fun onMinusItems(model: CartModel, position: Int)
    }

    private fun changeQty(holder: ViewHolder, data: CartModel) {
        holder.binding.etQty.setText(qtyItem.toString())
        data.quantity = qtyItem

        totalPrice = data.menu_price * qtyItem

        data.total_price = totalPrice + totalCountPrice

        Log.d("CEHCK ALSDA", data.total_price.toString())

        Log.d("CEHCK SDA", totalCountPrice.toString())

        itemCartCallback.onTotalQuantity(totalCountPrice,data.quantity)
    }
}