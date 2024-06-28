package com.example.buybuy.ui.mainscreen.productbycategoryFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProductByCategoryBinding
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Visible
import com.example.buybuy.util.setImage

class ProductByCategoryAdapter(private val list: List<ProductDetail>):RecyclerView.Adapter<ProductByCategoryAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemProductByCategoryBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(productDetail: ProductDetail){
            val newPrice=productDetail.price-(productDetail.price*productDetail.discount/100)
            binding.imageView.setImage(productDetail.image)
            binding.tvTitle.text=productDetail.title
            binding.tvCurrentPrice.text=newPrice.toString()+"$"
            if (productDetail.discount>0){
                binding.tvLastPrice.Visible()
                binding.tvLastPrice.text=productDetail.price.toString()+"$"
            }
            binding.tvLastPrice.paint.isStrikeThruText=true

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding=ItemProductByCategoryBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }
}