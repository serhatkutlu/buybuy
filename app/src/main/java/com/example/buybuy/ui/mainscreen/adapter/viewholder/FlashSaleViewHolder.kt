package com.example.buybuy.ui.mainscreen.adapter.viewholder

import android.os.CountDownTimer
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.buybuy.databinding.ItemFlashSaleRvBinding
import com.example.buybuy.domain.model.data.FlashSaleUiData
import com.example.buybuy.ui.mainscreen.adapter.TabContentAdapter
import com.example.buybuy.util.Resource
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.gone
import com.example.buybuy.util.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class FlashSaleViewHolder(private val binding: ItemFlashSaleRvBinding) : ViewHolder(binding.root) {

    var layoutManager: RecyclerView.LayoutManager? = null
    private val viewHolderScope = CoroutineScope(Dispatchers.Main + Job())
    fun bind(
        product: FlashSaleUiData,
        tabContentAdapter
        : TabContentAdapter,
        scrollState: Parcelable?,
    ) {
        binding.contentRecyclerView.adapter = tabContentAdapter
        binding.contentRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager = binding.contentRecyclerView.layoutManager
        binding.contentRecyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 35))


        initCountDown(product)
        tabContentAdapter.submitList(product.data)
//        viewHolderScope.launch {
//            product.collect {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.shimmer.startShimmer()
//                        binding.shimmer.visible()
//                        binding.contentRecyclerView.gone()
//                        binding.timeLayout.gone()
//                    }
//
//                    is Resource.Error -> {
//                        binding.shimmer.stopShimmer()
//                        binding.shimmer.gone()
//                        binding.contentRecyclerView.gone()
//                        binding.timeLayout.gone()
//                    }
//
//                    is Resource.Success -> {
//                        tabContentAdapter.submitList(it.data.data)
//                        binding.shimmer.stopShimmer()
//                        binding.shimmer.gone()
//                        binding.contentRecyclerView.visible()
//                        binding.timeLayout.visible()
//
//                    }
//                    else -> {}
//                }
//
//            }
//        }
        if (scrollState != null) {
            binding.contentRecyclerView.layoutManager?.onRestoreInstanceState(scrollState)
        } else {
            binding.contentRecyclerView.layoutManager?.scrollToPosition(0)
        }


    }

    private fun initCountDown(product: FlashSaleUiData) {
        val duration = Duration.between(LocalDateTime.now(), LocalDateTime.parse(product.endTime))
        val totalSeconds = duration.seconds
        startCountDown(totalSeconds)

    }

    private fun startCountDown(totalSeconds: Long) {
        val countDownTimer = object : CountDownTimer(totalSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                binding.tvHour.text = (secondsLeft /3600).toString()
                binding.tvMinute.text = ((secondsLeft % 3600)/60).toString()
                binding.tvSecond.text = (secondsLeft%60).toString()
            }

            override fun onFinish() {
            }
        }
        countDownTimer.start()
    }
}