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
import com.example.buybuy.databinding.ItemProductByCategoryBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.ui.mainscreen.adapter.MainRecycleViewAdapter
import com.example.buybuy.ui.productdetail.adapter.ProductDetailAdapter
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Visible
import com.example.buybuy.util.generateDiscount
import com.example.buybuy.util.setImage

class ProductByCategoryAdapter() :
    ListAdapter<ProductDetail, ProductByCategoryAdapter.ViewHolder>(
        ProductComparator()
    ) {

    var onClickListener: (ProductDetail) -> Unit = {}

    inner class ViewHolder(private val binding: ItemProductByCategoryBinding) :
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
        val binding = ItemProductByCategoryBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }





    override fun getItemCount(): Int {
        return currentList.size
    }


    class ProductComparator : DiffUtil.ItemCallback<ProductDetail>() {

        override fun areItemsTheSame(oldItem: ProductDetail, newItem: ProductDetail): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductDetail, newItem: ProductDetail): Boolean {
           return oldItem==newItem
        }
    }
}