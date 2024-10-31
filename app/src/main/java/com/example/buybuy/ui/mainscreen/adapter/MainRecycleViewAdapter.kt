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

class MainRecycleViewAdapter(
    val contentClickListener: (ProductDetailUI) -> Unit,
    val favoriteClickListener: (ProductDetailUI, Int, MainRecycleViewTypes) -> Unit,
    val fetchContentData: (content: String) -> Unit,

    ) :
    ListAdapter<MainRecycleViewTypes, ViewHolder>(ProductComparatorMainRV()) {


    private var selectedPageVpBanner = 1

    private var isFavoriteUpdateCategory = true
    private var scrollStateCategory: Parcelable? = null
    private var scrollStateFlashSale: Parcelable? = null

    var layoutManager: LayoutManager? = null
    var flashLayoutManager: LayoutManager? = null

    private var currentCategory: String? = null

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter()
    }
    private val tabContentAdapter: TabContentAdapter by lazy {
        TabContentAdapter(contentClickListener)
    }
    private val flashSaleAdapter: TabContentAdapter by lazy {
        TabContentAdapter(contentClickListener)
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
        scrollStateCategory = layoutManager?.onSaveInstanceState()
        scrollStateFlashSale=flashLayoutManager?.onSaveInstanceState()
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
                        scrollStateCategory,
                        favoriteClickListener
                    )
                    layoutManager = holder.layoutManager
                }

                is SingleBannerViewHolder -> {
                    holder.bind(item)
                }

                is FlashSaleViewHolder -> {
                    val data = item as MainRecycleViewTypes.FlashSaleDataUi
                    holder.bind(
                        item,
                        flashSaleAdapter,
                        favoriteClickListener,
                        scrollStateFlashSale,
                    )
                    flashLayoutManager=holder.layoutManager
                }

                else -> {

                }
            }
        }
    }

    private fun setCurrentCategory(category: String) {
        currentCategory = category
        fetchContentData(category)
        isFavoriteUpdateCategory = true

    }
//
//    private fun changeCategoryFavoriteItemPos(Product: ProductDetailUI?) {
//        Product?.let {
//            favoriteClickListener(Product)
//        }
//        //saveState()
//        //isFavoriteUpdateCategory = false
//    }

    fun updateCategoryItem(newItem: MainRecycleViewTypes) {
        val position = when (newItem) {

            is MainRecycleViewTypes.RVCategory -> {
                 currentList.indexOfFirst { it is MainRecycleViewTypes.RVCategory }
            }
            is MainRecycleViewTypes.FlashSaleDataUi -> {
                currentList.indexOfFirst { it is MainRecycleViewTypes.FlashSaleDataUi }
            }
            else -> {
                -1
            }
        }
        val currentList = currentList.toMutableList()
        currentList[position] = newItem
       submitList(currentList)
    }


    fun updateSingleProductItem(newItem: ProductDetailUI,position: Int,viewType:MainRecycleViewTypes) {

    when(viewType){
        is MainRecycleViewTypes.RVCategory->{
            tabContentAdapter.updateItem(newItem,position)
        }
        is MainRecycleViewTypes.FlashSaleDataUi->{
            flashSaleAdapter.updateItem(newItem,position)
        }
        else->{

        }
    }
    }


}
