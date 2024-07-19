package com.example.buybuy.domain.datasource.local

import com.example.buybuy.data.model.data.ProductDetail

interface LocalDataSource {

    suspend fun saveProducts(products: List<ProductDetail>)

    suspend fun searchProducts(query: String): List<ProductDetail>?

    suspend fun addToFavorite(product: ProductDetail)
    suspend fun deleteFromFavorite(product: Int)

    suspend fun getAllFavoriteProducts(): List<ProductDetail>
    suspend fun isProductFavorite(productId: Int): Boolean

}