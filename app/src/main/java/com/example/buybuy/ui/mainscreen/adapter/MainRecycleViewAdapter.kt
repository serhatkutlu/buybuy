package com.example.buybuy.ui.mainscreen.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
import com.example.buybuy.ui.mainscreen.adapter.viewholder.VpBannerViewHolderTest
import com.example.buybuy.util.ProductComparatorMainRV
import com.example.buybuy.util.Resource

class MainRecycleViewAdapter(
    val contentClickListener: (ProductDetailUI) -> Unit,
    val favoriteClickListener: (ProductDetailUI, Int, MainRecycleViewTypes) -> Unit,
    val fetchContentData: (content: String) -> Unit,

    ) :
    ListAdapter<MainRecycleViewTypes, ViewHolder>(ProductComparatorMainRV()) {




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
        scrollStateFlashSale = flashLayoutManager?.onSaveInstanceState()
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
                CategoryTabAndContentViewHolder(binding)
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
                FlashSaleViewHolder(binding)
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

    override fun onCurrentListChanged(
        previousList: MutableList<MainRecycleViewTypes>,
        currentList: MutableList<MainRecycleViewTypes>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].let { item ->
            when (holder) {
                is VpBannerViewHolder -> {
                    (holder).apply {
                        bind(item)
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
                    flashLayoutManager = holder.layoutManager
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


    fun updateRvItems(list: List<MainRecycleViewTypes>){
        val positions= mutableListOf<Int>()
        val newList = currentList.toMutableList()
        list.forEach{item->
            val position = currentList.indexOfFirst { it::class == item::class }
            if (position != -1) {
                positions.add(position)
                newList[position] = item

            }

        }
        submitList(newList){
            positions.forEach{
                notifyItemChanged(it)
            }
        }
    }
    fun updateCategoryItem(newItem: MainRecycleViewTypes) {
        val positionCategoryItem = currentList.indexOfFirst { it is MainRecycleViewTypes.RVCategory }
        if (positionCategoryItem != -1) {
            val currentList = currentList.toMutableList()
            currentList[positionCategoryItem] = newItem
            submitList(currentList){
             //   notifyItemChanged(positionCategoryItem)
            }

        }


    }
    fun updateFlashSaleItem(newItem: MainRecycleViewTypes) {
        val positionFlashSaleItem = currentList.indexOfFirst { it is MainRecycleViewTypes.FlashSaleDataUi }

        if (positionFlashSaleItem != -1) {
            val currentList = currentList.toMutableList()
            currentList[positionFlashSaleItem] = newItem
            submitList(currentList){
                notifyItemChanged(positionFlashSaleItem)
            }
        }
    }

    


    fun updateSingleProductItem(
        newItem: ProductDetailUI,
        position: Int,
        viewType: MainRecycleViewTypes
    ) {


        val currentList = currentList.toMutableList()


        when (viewType) {
            is MainRecycleViewTypes.RVCategory -> {
                val pos = currentList.indexOfFirst { it is MainRecycleViewTypes.RVCategory }
                if (pos != -1) {

                    currentList[pos] = viewType
                    submitList(currentList)
                    currentList[pos]
                    if (viewType.data is Resource.Success) {
                        tabContentAdapter.submitList(viewType.data.data)
                    }
                }
                tabContentAdapter.updateItem(newItem, position)

            }

            is MainRecycleViewTypes.FlashSaleDataUi -> {
                val pos = currentList.indexOfFirst { it is MainRecycleViewTypes.FlashSaleDataUi }
                if (pos != -1) {
                    currentList[pos] = viewType
                    submitList(currentList)
                }
                flashSaleAdapter.updateItem(newItem, position)
            }

            else -> {

            }
        }
    }


    override fun onViewRecycled(holder: ViewHolder) {
        if (holder is VpBannerViewHolder) {
            holder.stopAutoScroll()
        }
        super.onViewRecycled(holder)
    }


}
