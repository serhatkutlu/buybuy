package com.example.buybuy.data.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.ItemProductBinding
import com.example.buybuy.databinding.ItemProductShimmerPlaceholderBinding
import com.example.buybuy.util.ProductComparator
import com.example.buybuy.util.Visible
import com.example.buybuy.util.generateDiscount
import com.example.buybuy.util.setImage

class TabContentAdapter :
    ListAdapter<ProductDetail, TabContentAdapter.ContentViewHolder>(ProductComparator()) {
    var onClickListener: (ProductDetail) -> Unit = {}
    var onFavoriteClickListener: (ProductDetail,Int) -> Unit = {
        _,_->
    }
    var scrollstateChange: () -> Unit = {}
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onViewRecycled(holder: ContentViewHolder) {
        super.onViewRecycled(holder)
        scrollstateChange()

    }




    inner class ContentViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productDetail: ProductDetail) {


            val newPrice = productDetail.price generateDiscount productDetail.discount
            binding.imageView.setImage(productDetail.image)
            binding.tvTitle.text = productDetail.title
            binding.tvCurrentPrice.text = newPrice.toString() + "$"
            if (productDetail.discount > 0) {
                binding.tvLastPrice.Visible()
                binding.tvLastPrice.text = productDetail.price.toString() + "$"
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