package com.example.buybuy.ui.mainscreen.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.ui.mainscreen.adapter.VpBannerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VpBannerViewHolder(val binding: ItemVpBannerBinding) :
    RecyclerView.ViewHolder(binding.root) {
     var selectedPage = 1
    private val coroutineScopeAutoScroll  = CoroutineScope(Dispatchers.Main + Job())

    fun bind(item: MainRecycleViewTypes?) {
        item as MainRecycleViewTypes.VpBannerData
        val vpBannerAdapter = VpBannerAdapter()
        val infiniteList = item.data + item.data + item.data
        vpBannerAdapter.submitList(infiniteList)


        binding.viewPager.adapter = vpBannerAdapter

        val initialPosition = item.data.size
        binding.viewPager.setCurrentItem(initialPosition, false)

        startAutoScroll(binding)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                selectedPage = position % item.data.size
                binding.counterTextView.text = "${selectedPage + 1}/${item.data.size}"

                if (position == 0) {
                    binding.viewPager.setCurrentItem(infiniteList.size - (2 * item.data.size), false)
                } else if (position == infiniteList.size - 1) {
                    binding.viewPager.setCurrentItem(item.data.size -2, false)
                }
            }
        })


    }
    private fun startAutoScroll(binding: ItemVpBannerBinding) {

        coroutineScopeAutoScroll.coroutineContext.cancelChildren()
        coroutineScopeAutoScroll.launch {
            while (true) {
                delay(3000) // 3 saniye gecikme
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            }
        }
    }

    fun stopAutoScroll() {
        coroutineScopeAutoScroll.coroutineContext.cancelChildren()

    }

}