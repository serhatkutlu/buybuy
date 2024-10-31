package com.example.buybuy.ui.mainscreen.adapter.viewholder

import android.os.CountDownTimer
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.buybuy.databinding.ItemFlashSaleRvBinding
import com.example.buybuy.domain.model.data.FlashSaleUiData
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.ui.mainscreen.adapter.TabContentAdapter
import com.example.buybuy.util.Resource
import com.example.buybuy.util.SpacesItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDateTime

class FlashSaleViewHolder(private val binding: ItemFlashSaleRvBinding) : ViewHolder(binding.root) {

    var layoutManager: RecyclerView.LayoutManager? = null
    fun bind(
        item: MainRecycleViewTypes,
        flashSaleAdapter
        : TabContentAdapter,
        contentFavoriteClickListener: (ProductDetailUI,Int,MainRecycleViewTypes) -> Unit,
        scrollState: Parcelable?
    ) {

        item as MainRecycleViewTypes.FlashSaleDataUi

        binding.contentRecyclerView.adapter = flashSaleAdapter
        binding.contentRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager = binding.contentRecyclerView.layoutManager
        if (binding.contentRecyclerView.itemDecorationCount == 0) {
            binding.contentRecyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 35))
        }

        if (scrollState != null) {
            binding.contentRecyclerView.layoutManager?.onRestoreInstanceState(scrollState)
        } else {
            binding.contentRecyclerView.layoutManager?.scrollToPosition(0)
        }

        initCountDown(item.data)
        flashSaleAdapter.submitList(item.data.data)

        flashSaleAdapter.onFavoriteClickListener={ProductDetailUI,position->
            contentFavoriteClickListener(ProductDetailUI,position,item)
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
                binding.tvHour.text = (secondsLeft / 3600).toString()
                binding.tvMinute.text = ((secondsLeft % 3600) / 60).toString()
                binding.tvSecond.text = (secondsLeft % 60).toString()
            }

            override fun onFinish() {
            }
        }
        countDownTimer.start()
    }
}