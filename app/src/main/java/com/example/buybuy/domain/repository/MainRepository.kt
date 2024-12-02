package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.model.data.FlashSaleData
import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.domain.model.data.SingleBannerData
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes

import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getVpBannerData(): Flow<Resource<MainRecycleViewTypes.VpBannerData>>

    suspend fun getProductByCategory(category: String):Flow<Resource<List<ProductDetail>>>
    suspend fun getAllCategory():  Flow<Resource<List<String>>>
    suspend fun getAllSingleBanner(): Flow<Resource<List<SingleBannerData>>>
    suspend fun saveAllProduct(productDetail: List<ProductDetailEntity>)

    suspend fun searchProduct(query: String): Flow<Resource<List<ProductDetailEntity>>>
    suspend fun addToFavorite(productDetail: ProductDetailEntity):Boolean
    suspend fun deleteFromFavorite(productDetail: Int):Boolean
     suspend fun getAllFavorite(): Flow<Resource<List<ProductDetailEntity>>>
    suspend fun searchFavorites(query: String):Flow<Resource<List<ProductDetailEntity>>>

    suspend fun getAllProductFromDbWithCategory(category: String): Resource<List<ProductDetailEntity>>

    suspend fun isFavorite(productDetail: Int): Boolean

    suspend fun getAllFlashSaleProduct():Resource<FlashSaleData>
    suspend fun clearAllTables(): Flow<Resource<Unit>>


}