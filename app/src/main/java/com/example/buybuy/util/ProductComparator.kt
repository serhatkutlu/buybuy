package com.example.buybuy.util

import androidx.recyclerview.widget.DiffUtil
import com.example.buybuy.data.model.data.ProductDetail

class ProductComparator : DiffUtil.ItemCallback<ProductDetail>() {

    override fun areItemsTheSame(oldItem: ProductDetail, newItem: ProductDetail): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductDetail, newItem: ProductDetail): Boolean {
        return oldItem.isFavorite==newItem.isFavorite
    }
}