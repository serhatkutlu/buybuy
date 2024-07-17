package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.model.mainrecycleviewdata.RVCategory
import com.example.buybuy.domain.model.mainrecycleviewdata.VpBannerData
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getVpBannerData(): Flow<Resource<VpBannerData>>

    fun getProductByCategory(category: String):Flow<Resource<List<ProductDetail>>>
    fun getAllCategory():  Flow<Resource<RVCategory>>

    suspend fun saveAllProduct(productDetail: List<ProductDetail>)

    fun searchProduct(query: String): Flow<Resource<List<ProductDetail>>>
}