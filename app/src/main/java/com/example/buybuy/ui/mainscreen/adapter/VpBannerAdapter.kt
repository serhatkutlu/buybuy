package com.example.buybuy.ui.mainscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.databinding.ItemVpPageBinding
import com.example.buybuy.util.setImage

class VpBannerAdapter:ListAdapter<String,VpBannerAdapter.ViewPagerViewHolder>(BannerComparator()) {
    class ViewPagerViewHolder(val binding:ItemVpPageBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(image:String){
            binding.imageView.setImage(image)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerViewHolder {
        val binding=ItemVpPageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerViewHolder(binding)
    }



    override fun onBindViewHolder(holder:ViewPagerViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
         return currentList.size
    }
    class BannerComparator : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return false
        }
    }
}