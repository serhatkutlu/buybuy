package com.example.buybuy.ui.cartScreen.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProduct2Binding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.Visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class CartAdapter: ListAdapter<ProductDetail, CartAdapter.CartViewHolder>(ProductComparator()) {

     var productCartCountClickListener: (Boolean, Int) -> Unit = { _, _ -> }

    inner class CartViewHolder(private val binding:ItemProduct2Binding): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductDetail) {
            with(binding) {
                ivProduct.setImage(product.image)
                tvBrand.text = product.brand
                tvTitle.text = product.title
                rating.rating = product.star ?: 0f
                tvPrice.text = (product.star ?: 0f).toString()
                tvPrice.text = product.price.calculateDiscount(product.discount).toString()
                llCount.Visible()

                ivMinus.setOnClickListener {
                    if (product.cartCount > 0) {
                        product.cartCount--
                        tvCount.text = product.cartCount.toString()
                        productCartCountClickListener(false, product.id)
                    } else {
                        ivMinus.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.dark_grey
                            )
                        )
                    }
                }
                ivMinus.setOnClickListener {
                    product.cartCount++
                    tvCount.text = product.cartCount.toString()
                    productCartCountClickListener(true, product.id)
                    ivMinus.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.orange
                        )
                    )

                }


            }
        }
    }

    override fun getItemCount(): Int = currentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemProduct2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
                holder.bind(currentList[position])
       }
}