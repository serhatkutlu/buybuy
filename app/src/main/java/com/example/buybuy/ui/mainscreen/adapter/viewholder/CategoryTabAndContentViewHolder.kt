package com.example.buybuy.ui.mainscreen.adapter.viewholder

import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.ui.mainscreen.adapter.TabAdapter
import com.example.buybuy.ui.mainscreen.adapter.TabContentAdapter
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemCategoryContentRvBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.domain.model.mainrecycleviewdata.RVCategory
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.Visible
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
        item: MainRecycleViewdata,
        tabAdapter: TabAdapter,
        currentCategoryParent: String?,
        currentCategoryClickListener: (String) -> Unit,
        tabContentAdapter: TabContentAdapter,
        scrollState: Parcelable?,
        coroutineScope: CoroutineScope,
        fetchDataContent: (category: String) -> Flow<Resource<List<ProductDetail>>>,
        contentClickListener: (ProductDetail) -> Unit,
        contentFavoriteClickListener: (ProductDetail) -> Unit
    ) {
        val categoryItem = item as RVCategory
        currentCategory=
            if (currentCategoryParent != null) {
            currentCategoryParent
        } else {
            categoryItem.categories?.get(0)

        }
        binding.tabRecyclerView.adapter = tabAdapter
        binding.tabRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        tabAdapter.submitList(categoryItem.categories)
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
            result: Resource<List<ProductDetail>>,
            isNewData: Boolean,
            position: Int
        ) {
            withContext(Dispatchers.Main) {


                when (result) {
                    is Resource.Success -> {

                        tabContentAdapter.submitList(result.data)
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.Gone()
                        binding.contentRecyclerView.Visible()
                        if (!isNewData) {
                            tabContentAdapter.notifyItemChanged(position)
                        }
                    }

                    is Resource.Loading -> {
                        if (isNewData) {
                            tabContentAdapter.submitList(listOf())
                            shimmerFrameLayout.startShimmer()
                            shimmerFrameLayout.Visible()
                            binding.contentRecyclerView.Gone()
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