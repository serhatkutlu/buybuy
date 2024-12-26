package com.example.buybuy.ui.searchscreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.databinding.ItemProductBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class SearchScreenAdapter:ListAdapter<ProductDetailUI, SearchScreenAdapter.SearchScreenViewHolder>(
    ProductComparator()
){

    var onClickListener: (ProductDetailUI) -> Unit = {}
    var onFavoriteClickListener: (ProductDetailUI) -> Unit = {}

    inner class SearchScreenViewHolder(private val binding:ItemProductBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(productDetail: ProductDetailUI) {
            val context=binding.root.context
            val newPrice = productDetail.price calculateDiscount productDetail.discount
            binding.imageView.setImage(productDetail.image,true)
            binding.tvTitle.text = productDetail.title
            binding.tvCurrentPrice.text = context.getString(R.string.currency_symbol_detail,newPrice)

            setFavoriteBackground(productDetail.isFavorite)

            if (productDetail.discount > 0) {
                binding.tvLastPrice.visible()
                binding.tvLastPrice.text = context.getString(R.string.currency_symbol_detail,productDetail.price.toFloat())
            }
            binding.tvLastPrice.paint.isStrikeThruText = true

            binding.includedLayout.cvFavorite.setOnClickListener {
                onFavoriteClickListener(productDetail.copy())
                productDetail.isFavorite = !productDetail.isFavorite
                setFavoriteBackground(productDetail.isFavorite)
            }
            binding.cardView.setOnClickListener {
                onClickListener(productDetail)
            }

        }

         private fun setFavoriteBackground(changed: Boolean) {
            val color = if (changed) {
                ContextCompat.getColor(binding.root.context, R.color.orange)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.white)
            }

            binding.includedLayout.cvFavorite.setCardBackgroundColor(
                color
            )


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