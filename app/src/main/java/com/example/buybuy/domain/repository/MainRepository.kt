package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.model.data.mainrecycleviewdata.RVCategory
import com.example.buybuy.data.model.data.mainrecycleviewdata.VpBannerData
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getVpBannerData(): Flow<Resource<VpBannerData>>

    fun getProductByCategory(category: String):Flow<Resource<List<ProductDetail>>>
    fun getAllCategory():  Flow<Resource<RVCategory>>
}