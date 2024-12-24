package com.example.buybuy.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.databinding.ItemCheckoutAddressDialogBinding

class CheckoutAddressAdapter(private val list: List<AddressData>,private val onClickListener:(AddressData)->Unit):
    RecyclerView.Adapter<CheckoutAddressAdapter.CheckoutAddressViewHolder>() {

        inner class CheckoutAddressViewHolder(private val binding: ItemCheckoutAddressDialogBinding):RecyclerView.ViewHolder(binding.root) {
            fun bind(addressData: AddressData){
                binding.tvAddress.text=addressData.address
                binding.tvName.text=addressData.name+" "+addressData.surname
                binding.tvPhoneNumber.text=addressData.phone

                binding.root.setOnClickListener {
                    onClickListener(addressData)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAddressViewHolder {
        val binding=ItemCheckoutAddressDialogBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CheckoutAddressViewHolder(binding)
    }

    override fun getItemCount()=list.size

    override fun onBindViewHolder(holder: CheckoutAddressViewHolder, position: Int) {
        holder.bind(list[position])
    }
}