package com.example.buybuy.ui.mainscreen.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.databinding.ItemSingleBannerBinding
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.util.setImage

class SingleBannerViewHolder(val binding: ItemSingleBannerBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(data: MainRecycleViewTypes) {
        data as MainRecycleViewTypes.SingleBannerDataUi
        binding.ivImage.setImage(data.image)
    }
}