package com.example.buybuy.ui.mainscreen.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
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

    fun bind(item: MainRecycleViewTypes?, selectedPageChangeCallback: (Int) -> Int) {
        item as MainRecycleViewTypes.VpBannerData
        val vpBannerAdapter = VpBannerAdapter()
        val data = item.data.toMutableList()
        data.add(0, item.data[0])
        data.add(item.data.size - 1, item.data[item.data.size - 1])
        vpBannerAdapter.submitList(data)
        binding.viewPager.adapter = vpBannerAdapter
        startAutoScroll(binding)

        binding.viewPager.setCurrentItem(selectedPage, false)

        binding.counterTextView.text =
            (selectedPage).toString() + "/${item.data.size}"

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
                    (selectedPage).toString() + "/${item.data.size}"
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