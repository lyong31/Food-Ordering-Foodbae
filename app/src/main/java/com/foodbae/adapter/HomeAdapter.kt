package com.foodbae.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foodbae.databinding.ItemHomeBinding
import com.foodbae.model.FeelingModel
import com.foodbae.ui.menu.MenuTypeActivity
import com.foodbae.utils.SetImage.load

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var listFeeling = ArrayList<FeelingModel>()

    fun setData(items: List<FeelingModel>) {
        listFeeling.clear()
        listFeeling.addAll(items)
        this.notifyDataSetChanged()
    }

    class ViewHolder(private var binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FeelingModel) {
            binding.ivFeeling.load(data.image)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MenuTypeActivity::class.java)
                intent.putExtra(MenuTypeActivity.EXTRA_FEELING, data)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFeeling[position])
    }

    override fun getItemCount(): Int = listFeeling.size


}