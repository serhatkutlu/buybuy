package com.example.buybuy.ui.address.addressScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.databinding.ItemAddressBinding

class AddressRvAdapter():RecyclerView.Adapter<AddressRvAdapter.ViewHolder>() {

    private val list: MutableList<AddressData> = mutableListOf()
    var onClick: (AddressData?) -> Unit = {}

    inner class ViewHolder(private val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(addressData: AddressData){
            binding.nameSurname.text=addressData.name+" "+addressData.surname
            binding.phoneNumber.text=addressData.phone
            binding.address.text=addressData.address
            binding.addressName.text=addressData.addressName
            binding.root.setOnClickListener {
                onClick(addressData)
            }
        }
    }


    fun updateList(newList: List<AddressData>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}