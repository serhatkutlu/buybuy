package com.example.buybuy.ui.mainscreen.productbycategoryFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProductBinding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.Visible
import com.example.buybuy.util.generateDiscount
import com.example.buybuy.util.setImage

class ProductByCategoryAdapter() :
    ListAdapter<ProductDetail, ProductByCategoryAdapter.ViewHolder>(
        ProductComparator()
    ) {

    var onClickListener: (ProductDetail) -> Unit = {}

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productDetail: ProductDetail) {
            val newPrice = productDetail.price generateDiscount productDetail.discount
            binding.imageView.setImage(productDetail.image)
            binding.tvTitle.text = productDetail.title
            binding.tvCurrentPrice.text = newPrice.toString() + "$"
            if (productDetail.discount > 0) {
                binding.tvLastPrice.Visible()
                binding.tvLastPrice.text = productDetail.price.toString() + "$"
            }
            binding.tvLastPrice.paint.isStrikeThruText = true
            binding.cardView.setOnClickListener {
                onClickListener(productDetail)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }





    override fun getItemCount(): Int {
        return currentList.size
    }



}