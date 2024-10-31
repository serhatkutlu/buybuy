package com.example.buybuy.ui.mainscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.ItemProductBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class TabContentAdapter(private val onClickListener: (ProductDetailUI) -> Unit) :
    ListAdapter<ProductDetailUI, TabContentAdapter.ContentViewHolder>(ProductComparator()) {

    private var layoutManager:LayoutManager? = null
    var onFavoriteClickListener: (ProductDetailUI, Int) -> Unit={_,_->}
    inner class ContentViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productDetail: ProductDetailUI) {

            val context=binding.root.context
            val newPrice = productDetail.price calculateDiscount productDetail.discount
            binding.imageView.setImage(productDetail.image)
            binding.tvTitle.text = productDetail.title
            binding.tvCurrentPrice.text =context.getString(R.string.currency_symbol, newPrice.toString())
            if (productDetail.discount > 0) {
                binding.tvLastPrice.visible()
                binding.tvLastPrice.text =context.getString(R.string.currency_symbol, productDetail.price.toString())

            }
            binding.tvLastPrice.paint.isStrikeThruText = true
            binding.cardView.setOnClickListener {
                onClickListener(productDetail)
            }


            setFavoriteBackground(productDetail.isFavorite)

            binding.includedLayout.cvFavorite.setOnClickListener {
                val background =
                    binding.includedLayout.cvFavorite.cardBackgroundColor.defaultColor == ContextCompat.getColor(
                        binding.root.context,
                        R.color.orange
                    )

                onFavoriteClickListener(productDetail.copy(isFavorite = background), adapterPosition)

            }
        }

        private fun setFavoriteBackground(ischanged: Boolean) {
            val color = if (ischanged) {
                ContextCompat.getColor(binding.root.context, R.color.orange)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.white)
            }

            binding.includedLayout.cvFavorite.setCardBackgroundColor(
                color
            )


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {


        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ContentViewHolder(binding)

        return ContentViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (layoutManager == null) {
            layoutManager = holder.itemView.parent as? LayoutManager
    }}

    override fun onCurrentListChanged(
        previousList: MutableList<ProductDetailUI>,
        currentList: MutableList<ProductDetailUI>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }

    fun updateItem(item: ProductDetailUI, position: Int) {

        val currentList = currentList.toMutableList()
        currentList[position] = item
        submitList(currentList)

    }

}