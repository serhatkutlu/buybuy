package com.example.buybuy.util

import androidx.recyclerview.widget.DiffUtil
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.data.ProfileOption
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes

class ProductComparator : DiffUtil.ItemCallback<ProductDetailUI>() {

    override fun areItemsTheSame(oldItem: ProductDetailUI, newItem: ProductDetailUI): Boolean {
        return oldItem.hashCode()==newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: ProductDetailUI, newItem: ProductDetailUI): Boolean {
        return oldItem.id==newItem.id
    }
}

class ProductComparatorMainRV : DiffUtil.ItemCallback<MainRecycleViewTypes>() {
    override fun areItemsTheSame(oldItem: MainRecycleViewTypes, newItem: MainRecycleViewTypes) =
        oldItem.hashCode()==newItem.hashCode()

    override fun areContentsTheSame(
        oldItem: MainRecycleViewTypes,
        newItem: MainRecycleViewTypes
    ) =
        oldItem.type==newItem.type
}


class ProfileDiffUtil : DiffUtil.ItemCallback<ProfileOption>() {
    override fun areItemsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
        return oldItem.hashCode()==newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
        return oldItem.type==newItem.type
    }


}
