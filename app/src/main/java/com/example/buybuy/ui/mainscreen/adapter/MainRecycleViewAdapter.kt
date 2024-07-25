package com.example.buybuy.ui.mainscreen.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.buybuy.data.adapters.TabAdapter
import com.example.buybuy.data.adapters.TabContentAdapter
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemCategoryContentRvBinding
import com.example.buybuy.domain.model.enums.ViewType
import com.example.buybuy.databinding.ItemDividerMainRvBinding
import com.example.buybuy.databinding.ItemVpBannerBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.ui.mainscreen.adapter.viewholder.CategoryTabAndContentViewHolder
import com.example.buybuy.ui.mainscreen.adapter.viewholder.DividerViewHolder
import com.example.buybuy.ui.mainscreen.adapter.viewholder.VpBannerViewHolder
import com.example.buybuy.util.Resource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

class MainRecycleViewAdapter(

) :
    ListAdapter<MainRecycleViewdata, ViewHolder>(ProductComparator()) {


    private var selectedPageVpBanner = 1


    var contentClickListener: (ProductDetail) -> Unit = {}
    var contentFavoriteClickListener: (ProductDetail) -> Unit = {}
    lateinit var fetchContentData: (content: String) -> Flow<Resource<List<ProductDetail>>>

    private val coroutineScopeContent = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var scrollState: Parcelable? = null
    var layoutManager: LayoutManager? = null

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter()
    }
    private val tabContentAdapter: TabContentAdapter by lazy {
        TabContentAdapter()
    }


    override fun getItemViewType(position: Int): Int {

        return when (getItem(position).type) {
            ViewType.vp_banner -> {
                ViewType.vp_banner.ordinal
            }

            ViewType.category -> {
                ViewType.category.ordinal
            }

            else -> {
                ViewType.divider.ordinal
            }
        }


    }










    //
//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView)
//    }
//
//    override fun onViewDetachedFromWindow(holder: ViewHolder) {
//        super.onViewDetachedFromWindow(holder)
//    }
//    override fun onViewRecycled(holder: ViewHolder) {
//        super.onViewRecycled(holder)
//        if (holder is CategoryTabAndContentViewHolder) { // Cast işlemi
//            val key = currentList[holder.adapterPosition].type.toString()
//            scrollStates[key] = holder.layoutManager?.onSaveInstanceState() // LayoutManager'ı kullanın
//        }
//    }
    fun saveState() {
        scrollState = layoutManager?.onSaveInstanceState()
    }




    /*inner class CategoryViewHolder(val binding: ItemCategoryTablayoutAndViewpagerBinding) :
        ViewHolder(binding.root) {


        fun bind(item: MainRecycleViewdata) {
            val categoryItem = item as RVCategory

            with(binding) {
                categoryItem.categories?.let {


                    viewPager.adapter =
                        ViewPagerAdapter(fragment).apply { categories = categoryItem.categories }
                    viewPager.isUserInputEnabled = false
                    viewPager.offscreenPageLimit = 1



                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->

                        tab.setCustomView(R.layout.item_tab_layouth)
                        val textView = tab.customView?.findViewById<TextView>(R.id.tab_title)
                        textView?.text = categoryItem.categories[position]
                        if (position == 0) {
                            tab.customView?.findViewById<CardView>(R.id.cv_tab)
                                ?.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        fragment.requireContext(), R.color.orange
                                    )
                                )
                        }
                    }.attach()


                    val tabSelectedListener = object : TabLayout.OnTabSelectedListener {

                        override fun onTabSelected(tab: TabLayout.Tab?) {

                            selectedTabIndex = tab?.position ?: 0
                            tab?.customView?.findViewById<CardView>(R.id.cv_tab)
                                ?.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        fragment.requireContext(), R.color.orange
                                    )
                                )


                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                            tab?.customView?.findViewById<CardView>(R.id.cv_tab)
                                ?.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        fragment.requireContext(), R.color.white
                                    )
                                )

                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {}


                    }
                    tabLayout.getTabAt(0)?.apply {
                        //select()
                        tabSelectedListener.onTabUnselected(this)
                    }

                    tabLayout.getTabAt(selectedTabIndex)?.apply {
                        select()
                        tabSelectedListener.onTabSelected(this)
                    }

                    tabLayout.addOnTabSelectedListener(tabSelectedListener)
                }


            }
        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.vp_banner.ordinal -> {
                val binding = ItemVpBannerBinding.inflate(inflater, parent, false)
                VpBannerViewHolder(binding)
            }

            ViewType.category.ordinal -> {
                val binding =
                    ItemCategoryContentRvBinding.inflate(LayoutInflater.from(parent.context))
                return CategoryTabAndContentViewHolder(binding)
            }

            ViewType.divider.ordinal -> {
                val binding = ItemDividerMainRvBinding.inflate(inflater, parent, false)
                DividerViewHolder(binding)

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

        getItem(position).let {
            when (it.type) {
                ViewType.vp_banner -> {
                    (holder as VpBannerViewHolder).apply{
                        selectedPage=selectedPageVpBanner
                        bind(it){
                             selectedPageVpBanner = it
                            return@bind selectedPageVpBanner

                        }
                    }
                }

                ViewType.category -> {

                    (holder as CategoryTabAndContentViewHolder).bind(
                        it,
                        tabAdapter,
                        tabContentAdapter,
                        scrollState,
                        coroutineScopeContent,
                        fetchContentData,
                        contentClickListener,
                        contentFavoriteClickListener
                    )
                    layoutManager = holder.layoutManager
                }

                else -> {

                }
            }
        }
    }

    class ProductComparator : DiffUtil.ItemCallback<MainRecycleViewdata>() {
        override fun areItemsTheSame(oldItem: MainRecycleViewdata, newItem: MainRecycleViewdata) =
            true

        override fun areContentsTheSame(
            oldItem: MainRecycleViewdata,
            newItem: MainRecycleViewdata
        ) =
            true
    }


}
