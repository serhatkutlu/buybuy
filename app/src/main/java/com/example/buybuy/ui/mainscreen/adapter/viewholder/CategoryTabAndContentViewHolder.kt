package com.example.buybuy.ui.mainscreen.adapter.viewholder

import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.ui.mainscreen.adapter.TabAdapter
import com.example.buybuy.ui.mainscreen.adapter.TabContentAdapter
import com.example.buybuy.databinding.ItemCategoryContentRvBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes

import com.example.buybuy.util.Resource
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.invisible
import com.example.buybuy.util.visible
import com.example.buybuy.util.showToast

class CategoryTabAndContentViewHolder(private val binding: ItemCategoryContentRvBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var layoutManager: RecyclerView.LayoutManager? = null

    fun bind(
        item: MainRecycleViewTypes,
        tabAdapter: TabAdapter,
        currentCategory: String?,
        currentCategoryClickListener: (String) -> Unit,
        tabContentAdapter: TabContentAdapter,
        scrollState: Parcelable?,
        onFavoriteClickListener: (ProductDetailUI,Int,MainRecycleViewTypes) -> Unit,
    ){
        item as MainRecycleViewTypes.RVCategory

        binding.tabRecyclerView.adapter = tabAdapter
        binding.tabRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        tabAdapter.setSelectedPosition(item.categories.indexOf(currentCategory))
        tabAdapter.submitList(item.categories)

        binding.contentRecyclerView.adapter = tabContentAdapter
        binding.contentRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager = binding.contentRecyclerView.layoutManager


        if (binding.tabRecyclerView.itemDecorationCount == 0 && binding.contentRecyclerView.itemDecorationCount == 0) {
            binding.tabRecyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 30))
            binding.contentRecyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 35))
        }

        if (scrollState != null) {
            binding.contentRecyclerView.layoutManager?.onRestoreInstanceState(scrollState)
        } else {
            binding.contentRecyclerView.layoutManager?.scrollToPosition(0)
        }


        val shimmerFrameLayout = binding.shimmer



        when (item.data) {
            is Resource.Success -> {
                tabContentAdapter.submitList(item.data.data)
                shimmerFrameLayout.invisible()
                binding.contentRecyclerView.visible()

            }

            is Resource.Loading -> {
                    binding.contentRecyclerView.invisible()
                    shimmerFrameLayout.visible()
                    tabContentAdapter.submitList(listOf())
            }

            is Resource.Error -> {
                binding.root.context.showToast(item.data.message)
            }

            is Resource.Empty -> {}
        }


        tabAdapter.onTabSelected = {
            if (it != currentCategory) {
                currentCategoryClickListener(it)
            }

        }

        tabContentAdapter.onFavoriteClickListener={productDetailUI,position->
            onFavoriteClickListener(productDetailUI,position,item)
        }




    }
}