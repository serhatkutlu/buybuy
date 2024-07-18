package com.example.buybuy.ui.productdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.databinding.ItemProductDetailScreenBinding

class ProductDetailAdapter(val list:List<String>):RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemProductDetailScreenBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(it:String){
            binding.tvTitle.text=it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemProductDetailScreenBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount()=list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let {
            holder.bind(it)
        }
    }
}