package com.example.buybuy.data.source.local

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.datasource.local.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImp @Inject constructor(
    private val productDAO: ProductDAO,
) : LocalDataSource {
    override suspend fun saveProducts(products: List<ProductDetail>) {
        products.forEach {
            productDAO.insert(it)
        }

    }

    override suspend fun searchProducts(query: String): List<ProductDetail>? {
        return productDAO.searchAll(query)
    }

    override suspend fun addToFavorite(product: ProductDetail) {
        productDAO.addToFavorite(product.id)
    }

    override suspend fun deleteFromFavorite(product: Int) {
        productDAO.deleteFromFavorite(product)
    }

    override suspend fun getAllFavoriteProducts(): List<ProductDetail> =

        productDAO.getAllFavoriteProducts()


    override suspend fun isProductFavorite(productId: Int): Boolean =
        productDAO.isProductFavorite(productId)

    override suspend fun searchFavoriteProducts(query: String): List<ProductDetail>? {
       return productDAO.searchFavoriteProducts(query)
    }

    override suspend fun getCartProducts(): List<ProductDetail> {
        return productDAO.getCartProducts()
    }

    override suspend fun addToCart(product: Int) {
       productDAO.addToCart(product)
    }

    override suspend fun deleteFromCart(product: Int) {
        productDAO.deleteFromCart(product)
    }

    override suspend fun deleteAllCart() {
        productDAO.deleteCart()
    }


}