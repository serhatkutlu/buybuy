package com.example.buybuy.ui.favoritesscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.databinding.ItemProduct2Binding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class FavoritesAdapter:
    ListAdapter<ProductDetailUI, FavoritesAdapter.FavoritesViewHolder>(ProductComparator()) {


        var onItemClicked: (ProductDetailUI) -> Unit = {}
     inner class FavoritesViewHolder(val binding: ItemProduct2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDetailUI) {
            with(binding) {
                ivProduct.setImage(product.image)
                tvBrand.text = product.brand
                tvTitle.text = product.title
                rating.rating = product.star
                tvPrice.text = (product.star ).toString()
                tvPrice.text = product.price.calculateDiscount(product.discount).toString()
                tvCount.text = product.pieceCount.toString()
                buttonAddToCart.visible()
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