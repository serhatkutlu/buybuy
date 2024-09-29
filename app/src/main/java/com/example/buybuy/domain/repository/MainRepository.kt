package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes

import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getVpBannerData(): Flow<Resource<MainRecycleViewTypes.VpBannerData>>

    fun getProductByCategory(category: String):Flow<Resource<List<ProductDetail>>>
    fun getAllCategory():  Flow<Resource<MainRecycleViewTypes.RVCategory>>

    suspend fun saveAllProduct(productDetail: List<ProductDetail>)

    fun searchProduct(query: String): Flow<Resource<List<ProductDetail>>>
    suspend fun addToFavorite(productDetail: ProductDetail)
    suspend fun deleteFromFavorite(productDetail: Int)
     fun getAllFavorite(): Flow<Resource<List<ProductDetail>>>
    fun searchFavorites(query: String):Flow<Resource<List<ProductDetail>>>

    fun getCartProducts():Flow<Resource<List<ProductDetail>>>

    suspend fun  addToCart(product: Int)

    suspend fun reduceProductInCart(product: Int)
    suspend fun deleteProductFromCart(product: Int)

    suspend fun clearCart()
    suspend fun isFavorite(productDetail: Int): Boolean

}