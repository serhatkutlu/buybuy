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

    fun getVpBannerData(): Flow<Resource<MainRecycleViewTypes.VpBannerData>>

    fun getProductByCategory(category: String):Flow<Resource<List<ProductDetail>>>
    fun getAllCategory():  Flow<Resource<List<String>>>
    fun getAllSingleBanner(): Flow<Resource<List<SingleBannerData>>>
    suspend fun saveAllProduct(productDetail: List<ProductDetailEntity>)

    fun searchProduct(query: String): Flow<Resource<List<ProductDetailEntity>>>
    suspend fun addToFavorite(productDetail: ProductDetailEntity):Boolean
    suspend fun deleteFromFavorite(productDetail: Int):Boolean
     fun getAllFavorite(): Flow<Resource<List<ProductDetailEntity>>>
    fun searchFavorites(query: String):Flow<Resource<List<ProductDetailEntity>>>

    suspend fun getAllProductFromDbWithCategory(category: String): Resource<List<ProductDetailEntity>>
    fun getCartProducts():Flow<Resource<List<ProductDetailEntity>>>

    suspend fun  addToCart(product: Int)

    suspend fun reduceProductInCart(product: Int)
    suspend fun deleteProductFromCart(product: Int)

    suspend fun clearCart()
    suspend fun isFavorite(productDetail: Int): Boolean

    suspend fun getAllFlashSaleProduct():Resource<FlashSaleData>


}