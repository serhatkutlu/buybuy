package com.example.buybuy.ui.mainscreen.adapter

import android.annotation.SuppressLint
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.buybuy.R
import com.example.buybuy.data.model.data.Category
import com.example.buybuy.domain.model.mainrecycleviewdata.RVCategory
import com.example.buybuy.domain.model.mainrecycleviewdata.TlAndVpData
import com.example.buybuy.domain.model.mainrecycleviewdata.VpBannerData
import com.example.buybuy.domain.model.enums.ViewType
import com.example.buybuy.databinding.ItemCategoryTablayoutAndViewpagerBinding
import com.example.buybuy.databinding.ItemDividerMainRvBinding
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.ui.mainscreen.productbycategoryFragment.ProductByCategoryFragment
import com.example.buybuy.util.Constant
import com.example.buybuy.util.sharedPreferences
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class MainRecycleViewAdapter(
    private val fragment: Fragment
) :
    ListAdapter<MainRecycleViewdata, ViewHolder>(ProductComparator()) {


    private var selectedPage = 1
    private var selectedTabIndex = 0


    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var autoScrollJob: Job? = null


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
            val data = bannerItem.data.toMutableList()
            data.add(0, bannerItem.data[0])
            data.add(bannerItem.data.size - 1, bannerItem.data[bannerItem.data.size - 1])
            vpBannerAdapter.submitList(data)
            binding.viewPager.adapter = vpBannerAdapter
            startAutoScroll(binding)

            binding.viewPager.setCurrentItem(selectedPage, false)

            binding.counterTextView.text =
                (selectedPage).toString() + "/${bannerItem.data.size}"

            binding.viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    selectedPage = position
                    if (position == (binding.viewPager.adapter?.itemCount ?: 0) - 1) {
                        binding.viewPager.setCurrentItem(1, false)
                        selectedPage = 1

                    } else if (position == 0) {
                        binding.viewPager.setCurrentItem(
                            (binding.viewPager.adapter?.itemCount ?: 0) - 1, false
                        )
                        selectedPage = (binding.viewPager.adapter?.itemCount ?: 0) - 1
                    }

                    binding.counterTextView.text =
                        (selectedPage).toString() + "/${bannerItem.data.size}"
                }


            })


        }
    }

    private fun startAutoScroll(binding: ItemVpBannerBinding) {
        autoScrollJob = coroutineScope.launch {
            while (true) {
                delay(3000) // 3 saniye bekle

                binding.viewPager.setCurrentItem(++selectedPage, true)
            }
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

                        tab.setCustomView(R.layout.item_tab_layouth)
                        val textView = tab.customView?.findViewById<TextView>(R.id.tab_title)
                        textView?.text = categoryItem.categories[position]
                        if (position == 0) {
                            tab.customView?.findViewById<CardView>(R.id.cv_tab)
                                ?.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        fragment.requireContext(), R.color.orange
                                    )
                                )
                        }
                    }.attach()


                    val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            selectedTabIndex = tab?.position ?: 0
                            tab?.customView?.findViewById<CardView>(R.id.cv_tab)
                                ?.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        fragment.requireContext(), R.color.orange
                                    )
                                )

                            fragment.requireContext().sharedPreferences.edit()
                                .putInt(Constant.lASTRVPOS, 0).apply()

                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                            tab?.customView?.findViewById<CardView>(R.id.cv_tab)
                                ?.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        fragment.requireContext(), R.color.white
                                    )
                                )

                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {}


                    }
                    tabLayout.getTabAt(0)?.apply {
                        select()
                        tabSelectedListener.onTabUnselected(this)
                    }

                    tabLayout.getTabAt(selectedTabIndex)?.apply {
                        select()
                        tabSelectedListener.onTabSelected(this)
                    }

                    tabLayout.addOnTabSelectedListener(tabSelectedListener)
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
