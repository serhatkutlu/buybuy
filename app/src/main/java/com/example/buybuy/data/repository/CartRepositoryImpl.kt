package com.example.buybuy.data.repository

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.domain.datasource.local.ProductDataSource
import com.example.buybuy.domain.repository.CartRepository
import com.example.buybuy.util.Constant.NODATAFOUND
import com.example.buybuy.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CartRepositoryImpl(private val productDataSource: ProductDataSource,
): CartRepository {

    override fun getCartProducts(): Flow<Resource<List<ProductDetailEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val response = productDataSource.getCartProducts()
            if (response.isEmpty()) {
                emit(Resource.Error(NODATAFOUND))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun addToCart(product: Int) =flow{
        try {
            productDataSource.addToCart(product)
            emit(Resource.Success())
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }.flowOn(Dispatchers.IO)

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