package com.example.buybuy.data.repository.impl

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.data.repository.base.BaseRepository
import com.example.buybuy.domain.datasource.local.ProductDataSource
import com.example.buybuy.domain.repository.CartRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val productDataSource: ProductDataSource,
): CartRepository, BaseRepository(Dispatchers.IO) {

    override suspend fun getCartProducts(): Flow<Resource<List<ProductDetailEntity>>> =
        safeCall{
            productDataSource.getCartProducts()
        }


    override suspend fun addToCart(product: Int) =
        safeCall{
            productDataSource.addToCart(product)
        }


    override suspend fun reduceProductInCart(product: Int) {
        productDataSource.reduceProductInCart(product)
    }

    override suspend fun deleteProductFromCart(product: Int) {
        productDataSource.deleteProductFromCart(product)
    }

    override suspend fun clearCart() {
        productDataSource.deleteAllProductsFromCart()
    }
}