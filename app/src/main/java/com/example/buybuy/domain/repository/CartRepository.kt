package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun getCartProducts(): Flow<Resource<List<ProductDetailEntity>>>

    suspend fun  addToCart(product: Int): Flow<Resource<Unit>>

    suspend fun reduceProductInCart(product: Int)
    suspend fun deleteProductFromCart(product: Int)

    suspend fun clearCart()
}