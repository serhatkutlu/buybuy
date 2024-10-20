package com.example.buybuy.ui.mainscreen.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.buybuy.databinding.ItemCategoryContentRvBinding
import com.example.buybuy.enums.ViewType
import com.example.buybuy.databinding.ItemDividerMainRvBinding
import com.example.buybuy.databinding.ItemFlashSaleRvBinding
import com.example.buybuy.databinding.ItemSingleBannerBinding
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.ui.mainscreen.adapter.viewholder.CategoryTabAndContentViewHolder
import com.example.buybuy.ui.mainscreen.adapter.viewholder.DividerViewHolder
import com.example.buybuy.ui.mainscreen.adapter.viewholder.FlashSaleViewHolder
import com.example.buybuy.ui.mainscreen.adapter.viewholder.SingleBannerViewHolder
import com.example.buybuy.ui.mainscreen.adapter.viewholder.VpBannerViewHolder
import com.example.buybuy.util.ProductComparatorMainRV
import com.example.buybuy.util.Resource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

class MainRecycleViewAdapter :
    ListAdapter<MainRecycleViewTypes, ViewHolder>(ProductComparatorMainRV()) {


    private var selectedPageVpBanner = 1


    var contentClickListener: (ProductDetailUI) -> Unit = {}
    var contentFavoriteClickListener: (ProductDetailUI) -> Unit = {}
    lateinit var fetchContentData: (content: String) -> Flow<Resource<List<ProductDetailUI>>>

    private val coroutineScopeContent = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var scrollState: Parcelable? = null
    var layoutManager: LayoutManager? = null

    private var currentCategory: String? = null

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter()
    }
    private val tabContentAdapter: TabContentAdapter by lazy {
        TabContentAdapter()
    }
    private val flasSaleAdapter: TabContentAdapter by lazy {
        TabContentAdapter()
    }


    override fun getItemViewType(position: Int): Int {

        return when (getItem(position).type) {
            ViewType.VP_BANNER -> {
                ViewType.VP_BANNER.ordinal
            }

            ViewType.CATEGORY -> {
                ViewType.CATEGORY.ordinal
            }

            ViewType.SINGLE_BANNER -> {
                ViewType.SINGLE_BANNER.ordinal
            }

            ViewType.FLASH_SALE -> {
                ViewType.FLASH_SALE.ordinal
            }

            else -> {
                ViewType.DIVIDER.ordinal
            }
        }


    }


    fun saveState() {
        scrollState = layoutManager?.onSaveInstanceState()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.VP_BANNER.ordinal -> {
                val binding = ItemVpBannerBinding.inflate(inflater, parent, false)
                VpBannerViewHolder(binding)
            }

            ViewType.CATEGORY.ordinal -> {
                val binding =
                    ItemCategoryContentRvBinding.inflate(LayoutInflater.from(parent.context))
                return CategoryTabAndContentViewHolder(binding)
            }

            ViewType.SINGLE_BANNER.ordinal -> {
                val binding = ItemSingleBannerBinding.inflate(inflater, parent, false)
                SingleBannerViewHolder(binding)
            }

            ViewType.DIVIDER.ordinal -> {
                val binding = ItemDividerMainRvBinding.inflate(inflater, parent, false)
                DividerViewHolder(binding)

            }

            ViewType.FLASH_SALE.ordinal -> {
                val binding = ItemFlashSaleRvBinding.inflate(inflater, parent, false)
                return FlashSaleViewHolder(binding)
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

        getItem(position).let { item ->
            when (holder) {
                is VpBannerViewHolder -> {
                    (holder).apply {
                        selectedPage = selectedPageVpBanner
                        bind(item) {
                            selectedPageVpBanner = it
                            selectedPageVpBanner
                        }
                    }
                }

                is CategoryTabAndContentViewHolder -> {

                    holder.bind(
                        item,
                        tabAdapter,
                        currentCategory,
                        ::setCurrentCategory,
                        tabContentAdapter,
                        scrollState,
                        fetchContentData,
                        contentClickListener,
                        contentFavoriteClickListener
                    )
                    layoutManager = holder.layoutManager
                }

                is SingleBannerViewHolder -> {
                    holder.bind(item)
                }

                is FlashSaleViewHolder -> {
                    val data = item as MainRecycleViewTypes.FlashSaleDataUi
                    holder.bind(data.data, flasSaleAdapter, scrollState)
                }

                else -> {

                }
            }
        }
    }

    private fun setCurrentCategory(category: String) {
        currentCategory = category
    }


}
