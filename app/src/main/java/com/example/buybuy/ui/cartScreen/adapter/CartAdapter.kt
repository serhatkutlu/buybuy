package com.example.buybuy.ui.cartScreen.adapter

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProduct2Binding
import com.example.buybuy.enums.CartClickEnums
import com.example.buybuy.util.Constant.ALERT_CANCEL
import com.example.buybuy.util.Constant.ALERT_MESSAGE_CART
import com.example.buybuy.util.Constant.ALERT_TITLE_CART
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.Visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage
import com.example.buybuy.util.showAlertDialog

class CartAdapter :
    ListAdapter<ProductDetail, CartAdapter.CartViewHolder>(ProductComparator()) {

    var productCartCountClickListener: (CartClickEnums, Int) -> Unit = { _, _ -> }


    inner class CartViewHolder(private val binding: ItemProduct2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        private var pieceCount = 0
        fun bind(product: ProductDetail) {
            with(binding) {
                ivProduct.setImage(product.image)
                tvBrand.text = product.brand
                tvTitle.text = product.title
                tvRating.text = product.star.toString()
                rating.rating = product.star ?: 0f
                tvPrice.text = (product.star ?: 0f).toString()
                tvPrice.text = product.price.calculateDiscount(product.discount).toString()
                tvCount.text = product.pieceCount.toString()

                calculateIvMinusColor(product.pieceCount)

                llCount.Visible()

                pieceCount=product.pieceCount

                ivMinus.setOnClickListener {
                    Log.d("serhat", "bind: $pieceCount")
                    if (pieceCount == 1) return@setOnClickListener

                    productCartCountClickListener(CartClickEnums.MINUS, product.id)

                }
                ivPlus.setOnClickListener {
                    productCartCountClickListener(CartClickEnums.PLUS, product.id)


                }
                ivDelete.setOnClickListener {
                    binding.root.context.showAlertDialog(
                        ALERT_TITLE_CART,
                        ALERT_MESSAGE_CART,
                        negativeButtonText = ALERT_CANCEL,
                        positiveButtonAction = {
                            productCartCountClickListener(CartClickEnums.DELETE, product.id)

                        })
                }


            }
        }

        fun calculateIvMinusColor(count: Int) {
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