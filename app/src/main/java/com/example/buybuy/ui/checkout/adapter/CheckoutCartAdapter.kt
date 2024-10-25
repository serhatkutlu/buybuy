package com.example.buybuy.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.buybuy.databinding.ItemCheckoutCartItemBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class CheckoutCartAdapter():RecyclerView.Adapter<CheckoutCartAdapter.CartViewHolder>() {

    private val list:MutableList<ProductDetailUI> = mutableListOf()
     class CartViewHolder(private val binding: ItemCheckoutCartItemBinding):ViewHolder(binding.root){
         fun bind(productDetailUI: ProductDetailUI){
             binding.ivProductImage.setImage(productDetailUI.image)
             binding.tvPrice.text = productDetailUI.price.calculateDiscount(productDetailUI.discount).toString()
             binding.tvPieceCount.text=productDetailUI.pieceCount.toString()


         }
    }

    fun updateList(newList:List<ProductDetailUI>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding=ItemCheckoutCartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount()=list.size


}