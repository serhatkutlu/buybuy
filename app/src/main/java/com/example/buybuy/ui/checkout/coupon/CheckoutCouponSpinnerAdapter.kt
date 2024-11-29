package com.example.buybuy.ui.checkout.coupon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.buybuy.R
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.databinding.ItemCheckoutCouponSpinnerBinding

class CheckoutCouponSpinnerAdapter(
    private val context: Context,
    private val items: List<CouponData>
) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            ItemCheckoutCouponSpinnerBinding.inflate(inflater, parent, false)
        } else {
            ItemCheckoutCouponSpinnerBinding.bind(convertView)
        }

        binding.tvCode.text=items[position].name?:""
        binding.tvExpirationDate.text=items[position].expirationDate?:""
        if (!items[position].used) {
            binding.root.setBackgroundResource(R.drawable.ticket_bg)
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //if(items[position].name.isNullOrEmpty()) return View(context)
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            ItemCheckoutCouponSpinnerBinding.inflate(inflater, parent, false)
        } else {
            ItemCheckoutCouponSpinnerBinding.bind(convertView)
        }


        binding.tvCode.text=items[position].name
        binding.tvExpirationDate.text=items[position].expirationDate
        if (!items[position].used) binding.root.setBackgroundResource(R.drawable.ticket_bg)
        return binding.root
    }
}