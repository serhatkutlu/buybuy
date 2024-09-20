package com.example.buybuy.util

import androidx.recyclerview.widget.DiffUtil
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.model.data.ProfileOption
import com.example.buybuy.domain.model.MainRecycleViewdata

class ProductComparator : DiffUtil.ItemCallback<ProductDetail>() {

    override fun areItemsTheSame(oldItem: ProductDetail, newItem: ProductDetail): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductDetail, newItem: ProductDetail): Boolean {
        return oldItem==newItem
    }
}

class ProductComparatorMainRV : DiffUtil.ItemCallback<MainRecycleViewdata>() {
    override fun areItemsTheSame(oldItem: MainRecycleViewdata, newItem: MainRecycleViewdata) =
        oldItem.type==newItem.type

    override fun areContentsTheSame(
        oldItem: MainRecycleViewdata,
        newItem: MainRecycleViewdata
    ) =
        true
}


class ProfileDiffUtil : DiffUtil.ItemCallback<ProfileOption>() {
    override fun areItemsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
        return oldItem==newItem
    }


}
