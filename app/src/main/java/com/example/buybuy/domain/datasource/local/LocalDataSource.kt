package com.example.buybuy.domain.datasource.local

import com.example.buybuy.data.model.data.ProductDetail

interface LocalDataSource {

    suspend fun saveProducts(products: List<ProductDetail>)

    suspend fun searchProducts(query: String): List<ProductDetail>?

    suspend fun addToFavorite(product: ProductDetail)
    suspend fun deleteFromFavorite(product: Int)

    suspend fun getAllFavoriteProducts(): List<ProductDetail>
    suspend fun isProductFavorite(productId: Int): Boolean
    suspend fun searchFavoriteProducts(query: String): List<ProductDetail>?

    suspend fun getCartProducts(): List<ProductDetail>

    suspend fun addToCart(product: Int)

    suspend fun reduceProductInCart(product: Int)
    suspend fun deleteProductFromCart(product: Int)



    suspend fun deleteAllProductsFromCart()

}