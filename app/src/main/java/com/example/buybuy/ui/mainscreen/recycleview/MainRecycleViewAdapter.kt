package com.example.buybuy.ui.mainscreen.recycleview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.buybuy.data.model.data.VpBannerData
import com.example.buybuy.data.model.enums.ViewType
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.MainRecycleViewdata

class MainRecycleViewAdapter(private val items: List<MainRecycleViewdata>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (items[position].type == ViewType.vp_banner) {
            ViewType.vp_banner.ordinal
        } else {
            5
        }
    }

    inner class VpBannerViewHolder(val binding: ItemVpBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MainRecycleViewdata) {
            val bannerItem = item as VpBannerData

            binding.viewPager.adapter = VpBannerAdapter(bannerItem.data)
            binding.viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.counterTextView.text =
                        (position + 1).toString() + "/${bannerItem.data.size}"
                }


            })


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.vp_banner.ordinal -> {
                val binding = ItemVpBannerBinding.inflate(inflater, parent, false)
                VpBannerViewHolder(binding)
            }

            else -> {
                val binding = ItemVpBannerBinding.inflate(inflater, parent, false)
                VpBannerViewHolder(binding)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        items.get(position).let {
            when (it.type) {
                ViewType.vp_banner -> {
                    (holder as VpBannerViewHolder).bind(it)
                }

                else -> {

                }
            }
        }
    }



}
