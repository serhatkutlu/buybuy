package com.example.buybuy.ui.favoritesscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProduct2Binding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.Visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class FavoritesAdapter() :
    ListAdapter<ProductDetail, FavoritesAdapter.FavoritesViewHolder>(ProductComparator()) {


        var onItemClicked: (ProductDetail) -> Unit = {}
     inner class FavoritesViewHolder(val binding: ItemProduct2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDetail) {
            with(binding) {
                ivProduct.setImage(product.image)
                tvBrand.text = product.brand
                tvTitle.text = product.title
                rating.rating = product.star ?: 0f
                tvPrice.text = (product.star ?: 0f).toString()
                tvPrice.text = product.price.calculateDiscount(product.discount).toString()
                tvCount.text = product.pieceCount.toString()
                buttonAddToCart.Visible()
                buttonAddToCart.setOnClickListener {
                    onItemClicked(product)
                }



            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding =
            ItemProduct2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)

    }

    override fun getItemCount(): Int = currentList.size


    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}