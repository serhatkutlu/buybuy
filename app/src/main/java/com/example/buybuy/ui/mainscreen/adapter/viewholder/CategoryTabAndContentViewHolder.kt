package com.example.buybuy.ui.mainscreen.adapter.viewholder

import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.ui.mainscreen.adapter.TabAdapter
import com.example.buybuy.ui.mainscreen.adapter.TabContentAdapter
import com.example.buybuy.databinding.ItemCategoryContentRvBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes

import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.visible
import com.example.buybuy.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryTabAndContentViewHolder(private val binding: ItemCategoryContentRvBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var layoutManager: RecyclerView.LayoutManager? = null
    private var currentCategory: String? = null
    fun bind(
        item: MainRecycleViewTypes,
        tabAdapter: TabAdapter,
        currentCategoryParent: String?,
        currentCategoryClickListener: (String) -> Unit,
        tabContentAdapter: TabContentAdapter,
        scrollState: Parcelable?,
        coroutineScope: CoroutineScope,
        fetchDataContent: (category: String) -> Flow<Resource<List<ProductDetailUI>>>,
        contentClickListener: (ProductDetailUI) -> Unit,
        contentFavoriteClickListener: (ProductDetailUI) -> Unit
    ) {
        item as MainRecycleViewTypes.RVCategory
        currentCategory=currentCategoryParent ?: item.categories?.get(0)

        binding.tabRecyclerView.adapter = tabAdapter
        binding.tabRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        tabAdapter.submitList(item.categories)
        binding.tabRecyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 30))

        binding.contentRecyclerView.adapter = tabContentAdapter
        binding.contentRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager = binding.contentRecyclerView.layoutManager
        binding.contentRecyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 35))
        binding.contentRecyclerView.itemAnimator = null

        if (scrollState != null) {
            binding.contentRecyclerView.layoutManager?.onRestoreInstanceState(scrollState)
        } else {
            binding.contentRecyclerView.layoutManager?.scrollToPosition(0)
        }


        val shimmerFrameLayout = binding.shimmer
        suspend fun initCollect(
            result: Resource<List<ProductDetailUI>>,
            isNewData: Boolean,
            position: Int
        ) {
            withContext(Dispatchers.Main) {


                when (result) {
                    is Resource.Success -> {

                        tabContentAdapter.submitList(result.data)
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.gone()
                        binding.contentRecyclerView.visible()
                        if (!isNewData) {
                            tabContentAdapter.notifyItemChanged(position)
                        }
                    }

                    is Resource.Loading -> {
                        if (isNewData) {
                            tabContentAdapter.submitList(listOf())
                            shimmerFrameLayout.startShimmer()
                            shimmerFrameLayout.visible()
                            binding.contentRecyclerView.gone()
                        }
                    }

                    is Resource.Error -> {
                        binding.root.context.showToast(result.message)
                    }
                    is Resource.Empty->{}
                }
            }
        }

        fun initSearchData(category: String, isNewData: Boolean, position: Int = 0) {
            coroutineScope.launch {
                fetchDataContent(
                    category
                ).collect { initCollect(it, isNewData, position) }
            }
        }

        initSearchData(currentCategory ?: "", false)


        tabAdapter.onTabSelected = {
            currentCategory = it
            initSearchData(it, true)
            currentCategoryClickListener(it)
        }

        tabContentAdapter.onClickListener = contentClickListener
        tabContentAdapter.onFavoriteClickListener = { product, position ->
            contentFavoriteClickListener(product)
            initSearchData(currentCategory ?: "", false, position)
        }


    }
}