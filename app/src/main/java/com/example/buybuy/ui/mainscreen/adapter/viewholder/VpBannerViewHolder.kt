package com.example.buybuy.ui.mainscreen.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.domain.model.mainrecycleviewdata.VpBannerData
import com.example.buybuy.ui.mainscreen.adapter.VpBannerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VpBannerViewHolder(val binding: ItemVpBannerBinding) :
    RecyclerView.ViewHolder(binding.root) {
     var selectedPage = 1
    private val coroutineScopeScroll = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var autoScrollJob: Job? = null

    fun bind(item: MainRecycleViewdata?,selectedPageChangeCallback: (Int) -> Int) {
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
                selectedPage=selectedPageChangeCallback(position)
                if (position == (binding.viewPager.adapter?.itemCount ?: 0) - 1) {
                    binding.viewPager.setCurrentItem(1, false)
                    selectedPage=selectedPageChangeCallback(1)

                } else if (position == 0) {
                    binding.viewPager.setCurrentItem(
                        (binding.viewPager.adapter?.itemCount ?: 0) - 1, false
                    )
                   selectedPage= selectedPageChangeCallback((binding.viewPager.adapter?.itemCount ?: 0) - 1)
                }

                binding.counterTextView.text =
                    (selectedPage).toString() + "/${bannerItem.data.size}"
            }


        })


    }
    private fun startAutoScroll(binding: ItemVpBannerBinding) {

        autoScrollJob?.cancel()
        autoScrollJob = coroutineScopeScroll.launch {
            while (true) {
                delay(3000)
                binding.viewPager.setCurrentItem(++selectedPage, true)
            }
        }
    }

}