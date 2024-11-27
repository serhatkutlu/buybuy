package com.example.buybuy.ui.orderscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.databinding.ItemProduct2Binding
import com.example.buybuy.domain.model.data.OrderDataUi
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage
import com.example.buybuy.util.visible

class MyOrdersAdapter(private var orderList: List<OrderDataUi>, private val onItemClicked: (ProductDetailUI) -> Unit) :
    RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder>() {

    inner class MyOrdersViewHolder(private val binding: ItemProduct2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: OrderDataUi) {
            with(binding) {
                with(product.data) {
                    ivProduct.setImage(image,true)
                    tvBrand.text = brand
                    tvTitle.text = title
                    rating.rating = star
                    tvPrice.text = star.toString()
                    tvPrice.text = binding.root.context.getString(
                        R.string.currency_symbol_detail,
                        price.calculateDiscount(discount)
                    )
                    tvPieceCount.text = binding.root.context.getString(R.string.fragment_my_orders_quantity,product.piece.toString())
                    tvDate.text=product.time
                    llOrderDetails.visible()
                    buttonRate.visible()
                    buttonRate.setOnClickListener{
                        onItemClicked(product.data)
                    }


                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val binding =
            ItemProduct2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOrdersViewHolder(binding)
    }

    override fun getItemCount() = orderList.size


     fun updateList(list: List<OrderDataUi>) {
         orderList=list
         notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        holder.bind(orderList[position])
    }
}