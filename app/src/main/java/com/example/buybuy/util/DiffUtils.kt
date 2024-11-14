package com.example.buybuy.util

import androidx.recyclerview.widget.DiffUtil
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.data.ProfileOption
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes

class ProductComparator : DiffUtil.ItemCallback<ProductDetailUI>() {

    override fun areItemsTheSame(oldItem: ProductDetailUI, newItem: ProductDetailUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductDetailUI, newItem: ProductDetailUI): Boolean {
        return oldItem.isFavorite == newItem.isFavorite
    }
}
class ProductComparatorCart : DiffUtil.ItemCallback<ProductDetailUI>() {

    override fun areItemsTheSame(oldItem: ProductDetailUI, newItem: ProductDetailUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductDetailUI, newItem: ProductDetailUI): Boolean {
        return oldItem.pieceCount == newItem.pieceCount
    }
}

class ProductComparatorMainRV : DiffUtil.ItemCallback<MainRecycleViewTypes>() {
    override fun areItemsTheSame(oldItem: MainRecycleViewTypes, newItem: MainRecycleViewTypes):Boolean {
        return oldItem.type == newItem.type
    }
    override fun areContentsTheSame(
        oldItem: MainRecycleViewTypes,
        newItem: MainRecycleViewTypes
    ): Boolean {

        return if (newItem is MainRecycleViewTypes.RVCategory && oldItem is MainRecycleViewTypes.RVCategory ) {

            if (newItem.currentCategory != oldItem.currentCategory) false
            else if (newItem.data is Resource.Success&&oldItem.data is Resource.Success){
                (newItem.data.data?.get(0)?.id ?: -1) == (oldItem.data.data?.get(0)?.id ?:-1)
            } else {
                newItem.data == oldItem.data
            }

        }
        else if (newItem is MainRecycleViewTypes.FlashSaleDataUi && oldItem is MainRecycleViewTypes.FlashSaleDataUi){
            newItem.data.data.size==oldItem.data.data.size
            //return newItem.data.data.zip(oldItem.data.data).all { (item1, item2) -> item1.isFavorite == item2.isFavorite }
        }

        else newItem.type==oldItem.type
    }
}


class ProfileDiffUtil : DiffUtil.ItemCallback<ProfileOption>() {
    override fun areItemsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
        return oldItem.type == newItem.type
    }


}

class CouponDiffUtil : DiffUtil.ItemCallback<CouponData>() {
    override fun areItemsTheSame(oldItem: CouponData, newItem: CouponData): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: CouponData, newItem: CouponData): Boolean {
        return oldItem.used == newItem.used
    }
}


