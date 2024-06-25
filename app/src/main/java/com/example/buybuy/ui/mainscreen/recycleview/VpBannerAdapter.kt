package com.example.buybuy.ui.mainscreen.recycleview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.databinding.ItemVpPageBinding
import com.example.buybuy.util.setImage

class VpBannerAdapter(private val images:List<String>):RecyclerView.Adapter<VpBannerAdapter.ViewPagerViewHolder>() {
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
        images.get(position).let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        Log.d("serhat", "getItemCount: ${images.size}")
         return images.size
    }
}