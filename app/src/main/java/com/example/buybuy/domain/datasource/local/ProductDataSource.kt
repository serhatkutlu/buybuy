package com.example.buybuy.domain.datasource.local

import com.example.buybuy.data.model.entity.ProductDetailEntity

interface ProductDataSource {

    suspend fun saveProducts(products: List<ProductDetailEntity>)

    suspend fun searchProducts(query: String): List<ProductDetailEntity>?

    suspend fun addToFavorite(product: ProductDetailEntity)
    suspend fun deleteFromFavorite(product: Int)
    suspend fun getAllProductsWithCategory(category: String): List<ProductDetailEntity>
    suspend fun getAllFavoriteProducts(): List<ProductDetailEntity>
    suspend fun isProductFavorite(productId: Int): Boolean
    suspend fun searchFavoriteProducts(query: String): List<ProductDetailEntity>?

    suspend fun getCartProducts(): List<ProductDetailEntity>

    suspend fun addToCart(product: Int)

    suspend fun reduceProductInCart(product: Int)
    suspend fun deleteProductFromCart(product: Int)

    suspend fun deleteAllProductsFromCart()

}