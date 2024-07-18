package com.example.buybuy.ui.searchscreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProductBinding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.Visible
import com.example.buybuy.util.generateDiscount
import com.example.buybuy.util.setImage

class SearchScreenAdapter():ListAdapter<ProductDetail, SearchScreenAdapter.SearchScreenViewHolder>(ProductComparator()){

    var onClickListener: (ProductDetail) -> Unit = {}
    inner class SearchScreenViewHolder(private val binding:ItemProductBinding):RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchScreenViewHolder {
        val binding=ItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchScreenViewHolder(binding)
    }

    override fun getItemCount()=currentList.size

    override fun onBindViewHolder(holder: SearchScreenViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


}