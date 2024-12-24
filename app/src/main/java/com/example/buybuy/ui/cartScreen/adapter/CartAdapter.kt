package com.example.buybuy.ui.cartScreen.adapter

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.databinding.ItemProduct2Binding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.enums.CartClickEnums

import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.ProductComparatorCart
import com.example.buybuy.util.visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage
import com.example.buybuy.util.showAlertDialog

class CartAdapter :
    ListAdapter<ProductDetailUI, CartAdapter.CartViewHolder>(ProductComparatorCart()) {

    var productCartCountClickListener: (CartClickEnums, Int) -> Unit = { _, _ -> }


    inner class CartViewHolder(private val binding: ItemProduct2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        private var pieceCount = 0

        fun bind(product: ProductDetailUI) {
            var isReduced=false

            with(binding) {

                val discount = product.discount
                ivProduct.setImage(product.image,true)
                tvBrand.text = product.brand
                tvTitle.text = product.title
                tvRating.text = product.star.toString()
                rating.rating = product.star
                tvPrice.text = (product.star).toString()
                tvPrice.text = binding.root.context.getString(R.string.currency_symbol,product.price.calculateDiscount(discount))
                tvCount.text = product.pieceCount.toString()

                calculateIvMinusColor(product.pieceCount)

                llCount.visible()

                pieceCount=product.pieceCount

                ivMinus.setOnClickListener {
                    if (!isReduced&&pieceCount!=1){
                        isReduced=true
                        productCartCountClickListener(CartClickEnums.MINUS, product.id)
                    }
                }
                ivPlus.setOnClickListener {
                    productCartCountClickListener(CartClickEnums.PLUS, product.id)


                }
                ivDelete.setOnClickListener {
                    binding.root.context.apply{
                        showAlertDialog(
                            getString(R.string.alert_title_cart),
                            getString(R.string.alert_message_cart),
                            negativeButtonText = getString(R.string.alert_cancel),
                            positiveButtonAction = {
                                productCartCountClickListener(CartClickEnums.DELETE, product.id)

                            })
                    }
                }


            }
        }

        private fun calculateIvMinusColor(count: Int) {
            if (count <= 1) {
                binding.ivMinus.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.dark_grey
                    )
                )
            } else {
                binding.ivMinus.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.orange
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = currentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            ItemProduct2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}