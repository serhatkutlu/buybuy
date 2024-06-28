package com.example.buybuy.ui.mainscreen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.mainrecycleviewdata.RVCategory
import com.example.buybuy.data.model.data.mainrecycleviewdata.TlAndVpData
import com.example.buybuy.data.model.data.mainrecycleviewdata.VpBannerData
import com.example.buybuy.data.model.enums.ViewType
import com.example.buybuy.databinding.ItemCategoryTablayoutAndViewpagerBinding
import com.example.buybuy.databinding.ItemDividerMainRvBinding
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.ui.mainscreen.productbycategoryFragment.ProductByCategoryFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainRecycleViewAdapter(
    private val items: List<MainRecycleViewdata>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (items.get(position).type) {
            ViewType.vp_banner -> {
                ViewType.vp_banner.ordinal
            }

            ViewType.category -> {
                ViewType.category.ordinal
            }

            else -> {
                ViewType.divider.ordinal
            }
        }


    }


    class VpBannerViewHolder(val binding: ItemVpBannerBinding) :
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

    class DividerViewHolder(binding: ItemDividerMainRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CategoryViewHolder(val binding: ItemCategoryTablayoutAndViewpagerBinding) :
        ViewHolder(binding.root) {
        fun bind(item: MainRecycleViewdata) {
            val categoryItem = item as RVCategory
            with(binding) {
                categoryItem.categories?.let {
                    viewPager.adapter =
                        ViewPagerAdapter(fragment, categoryItem.categories)
                    viewPager.isUserInputEnabled = false
                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                        tab.text = categoryItem.categories[position]
                    }.attach()

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.vp_banner.ordinal -> {
                val binding = ItemVpBannerBinding.inflate(inflater, parent, false)
                VpBannerViewHolder(binding)
            }

            ViewType.category.ordinal -> {
                val binding =
                    ItemCategoryTablayoutAndViewpagerBinding.inflate(LayoutInflater.from(parent.context))
                return CategoryViewHolder(binding)
            }

            ViewType.divider.ordinal -> {
                val binding = ItemDividerMainRvBinding.inflate(inflater, parent, false)
                DividerViewHolder(binding)

            }

            else -> {
                val binding = ItemDividerMainRvBinding.inflate(inflater, parent, false)
                DividerViewHolder(binding)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        items.get(position).let {
            when (it.type) {
                ViewType.vp_banner -> {
                    (holder as VpBannerViewHolder).bind(it)
                }

                ViewType.category -> {

                    (holder as CategoryViewHolder).bind(it)
                }

                else -> {

                }
            }
        }
    }


}
