package com.example.buybuy.ui.favoritesscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemFavoriteBinding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class FavoritesAdapter():
    ListAdapter<ProductDetail,FavoritesAdapter.FavoritesViewHolder>(ProductComparator()) {
    class FavoritesViewHolder(val binding: ItemFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDetail){
            with(binding){
                ivProduct.setImage(product.image)
                tvBrand.text = product.brand
                tvTitle.text = product.title
                tvPrice.text = product.price.calculateDiscount(product.discount).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)

    }

    override fun getItemCount(): Int =currentList.size


    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}