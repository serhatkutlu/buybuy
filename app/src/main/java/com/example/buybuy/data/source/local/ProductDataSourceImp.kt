package com.example.buybuy.data.source.local

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.domain.datasource.local.ProductDataSource
import javax.inject.Inject

class ProductDataSourceImp @Inject constructor(
    private val productDAO: ProductDAO,
) : ProductDataSource {
    override suspend fun saveProducts(products: List<ProductDetailEntity>) {
        products.forEach {
            productDAO.insert(it)
        }

    }

    override suspend fun searchProducts(query: String): List<ProductDetailEntity>? {
        return productDAO.searchAll(query)
    }

    override suspend fun addToFavorite(product: ProductDetailEntity) {
        productDAO.addToFavorite(product.id)
    }

    override suspend fun deleteFromFavorite(product: Int) {
        productDAO.deleteFromFavorite(product)
    }

    override suspend fun getAllProductsWithCategory(category: String): List<ProductDetailEntity> {
        return productDAO.getAllProducts(category)
    }

    override suspend fun getAllFavoriteProducts(): List<ProductDetailEntity> =

        productDAO.getAllFavoriteProducts()


    override suspend fun isProductFavorite(productId: Int): Boolean =
        productDAO.isProductFavorite(productId)

    override suspend fun searchFavoriteProducts(query: String): List<ProductDetailEntity>? {
       return productDAO.searchFavoriteProducts(query)
    }

    override suspend fun getCartProducts(): List<ProductDetailEntity> {
        return productDAO.getCartProducts()
    }

    override suspend fun addToCart(product: Int) {
       productDAO.addToCart(product)
    }

    override suspend fun reduceProductInCart(product: Int) {
        productDAO.reduceProductInCart(product)
    }

    override suspend fun deleteProductFromCart(product: Int) {
        productDAO.deleteProductFromCart(product)
    }


    override suspend fun deleteAllProductsFromCart() {
        productDAO.deleteAllProductCart()
    }


}