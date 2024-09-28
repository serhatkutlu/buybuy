package com.example.buybuy.ui.mainscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProductBinding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage

class TabContentAdapter :
    ListAdapter<ProductDetail, TabContentAdapter.ContentViewHolder>(ProductComparator()) {
    var onClickListener: (ProductDetail) -> Unit = {}
    var onFavoriteClickListener: (ProductDetail,Int) -> Unit = {
        _,_->
    }
    private var layoutManager:LayoutManager? = null





    inner class ContentViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productDetail: ProductDetail) {

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
                //setFavoriteBackground(!background)
                onFavoriteClickListener(productDetail.copy(isFavorite = background),adapterPosition)
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
        previousList: MutableList<ProductDetail>,
        currentList: MutableList<ProductDetail>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }


}