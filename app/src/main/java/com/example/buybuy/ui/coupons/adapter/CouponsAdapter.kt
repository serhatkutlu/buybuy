package com.example.buybuy.ui.coupons.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.databinding.ItemTicketBinding
import com.example.buybuy.util.CouponDiffUtil

class CouponsAdapter(): ListAdapter<CouponData, CouponsAdapter.ViewHolder>(CouponDiffUtil())  {

    class ViewHolder(private val binding:ItemTicketBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(data:CouponData){
            binding.tvCode.text=data.name
            binding.tvDiscount.text=binding.root.context.getString(R.string.percentage_symbol,data.discount)
            binding.tvExpirationDate.text=data.expirationDate

            if (data.used==true){
                binding.root.background = ColorDrawable(Color.DKGRAY)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemTicketBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}