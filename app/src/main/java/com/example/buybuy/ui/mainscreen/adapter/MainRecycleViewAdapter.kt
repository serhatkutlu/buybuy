package com.example.buybuy.ui.mainscreen.adapter

import android.annotation.SuppressLint
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
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
import com.example.buybuy.util.Constant
import com.example.buybuy.util.sharedPreferences
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Exception

class MainRecycleViewAdapter(
    private val fragment: Fragment
) :
    ListAdapter<MainRecycleViewdata, ViewHolder>(ProductComparator()) {


    private var selectedPage = 0
    private var selectedTabIndex = 0
    private var selectedTabPage = 0


    override fun getItemViewType(position: Int): Int {

        return when (getItem(position).type) {
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

    fun update(list: List<MainRecycleViewdata>) {
        submitList(list)
    }


    inner class VpBannerViewHolder(val binding: ItemVpBannerBinding) :
        ViewHolder(binding.root) {
        fun bind(item: MainRecycleViewdata) {
            val bannerItem = item as VpBannerData
            val vpBannerAdapter = VpBannerAdapter()
            vpBannerAdapter.submitList(bannerItem.data)
            binding.viewPager.adapter = vpBannerAdapter
            binding.viewPager.setCurrentItem(selectedPage, false)

            binding.counterTextView.text =
                (selectedPage + 1).toString() + "/${bannerItem.data.size}"

            binding.viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    selectedPage = position
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
        val categoryAdapter by lazy { ViewPagerAdapter(fragment) }
        fun bind(item: MainRecycleViewdata) {
            val categoryItem = item as RVCategory

            with(binding) {
                categoryItem.categories?.let {

                    categoryAdapter.categories = categoryItem.categories
                    viewPager.adapter = categoryAdapter
                    viewPager.isUserInputEnabled = false


                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                        tab.text = categoryItem.categories[position]
                    }.attach()

                    tabLayout.getTabAt(selectedTabIndex)?.select()
                    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            selectedTabIndex = tab?.position ?: 0

                            fragment.requireContext().sharedPreferences.edit()
                                .putInt(Constant.lASTRVPOS, 0).apply()

                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {}
                        override fun onTabReselected(tab: TabLayout.Tab?) {}


                    })
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
        return currentList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        getItem(position).let {
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

    class ProductComparator : DiffUtil.ItemCallback<MainRecycleViewdata>() {
        override fun areItemsTheSame(oldItem: MainRecycleViewdata, newItem: MainRecycleViewdata) =
            true

        override fun areContentsTheSame(
            oldItem: MainRecycleViewdata,
            newItem: MainRecycleViewdata
        ) =
            true
    }


}
