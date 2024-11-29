package com.example.buybuy.ui.coupons.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.set
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.databinding.ItemTicketBinding
import com.example.buybuy.util.CouponDiffUtil
import com.example.buybuy.util.formatReadableDate

class CouponsAdapter(): ListAdapter<CouponData, CouponsAdapter.ViewHolder>(CouponDiffUtil())  {

    class ViewHolder(private val binding:ItemTicketBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(data:CouponData){
            binding.tvCode.text=data.name
            binding.tvDiscount.text=binding.root.context.getString(R.string.percentage_symbol,data.discount)
            binding.tvExpirationDate.text=setTextColor(binding.root.context,data.expirationDate?.formatReadableDate()?:"0")

            if (data.used){
                binding.root.background = ColorDrawable(Color.DKGRAY)
            }
        }


        private fun setTextColor(context: Context, date:String):SpannableString{
            val length=context.getString(R.string.item_ticket_expiration_date).indexOf("%s")
            val text=context.getString(R.string.item_ticket_expiration_date,date)
            return SpannableString(text).apply{
                setSpan(ForegroundColorSpan(context.getColor(R.color.light_grey)),0,length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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