package com.example.buybuy.ui.mainscreen.productbycategoryFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProductBinding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.Visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class ProductByCategoryAdapter() :
    ListAdapter<ProductDetail, ProductByCategoryAdapter.ViewHolder>(
        ProductComparator()
    ) {

    var onClickListener: (ProductDetail) -> Unit = {}
    var onFavoriteClickListener: (ProductDetail) -> Unit = {}

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productDetail: ProductDetail) {
            val newPrice = productDetail.price calculateDiscount productDetail.discount
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
            setFavoriteBackground(productDetail.isFavorite)

            binding.includedLayout.cvFavorite.setOnClickListener {
                val background = binding.includedLayout.cvFavorite.cardBackgroundColor.defaultColor==ContextCompat.getColor(binding.root.context, R.color.orange)
                setFavoriteBackground(!background)
                onFavoriteClickListener(productDetail.copy(isFavorite = background))
            }

        }

        private fun setFavoriteBackground(ischanged: Boolean) {
            val color = if (ischanged) {
                ContextCompat.getColor(binding.root.context, R.color.orange)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.white)
            }

            binding.includedLayout.cvFavorite.setCardBackgroundColor(
                color
            )


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