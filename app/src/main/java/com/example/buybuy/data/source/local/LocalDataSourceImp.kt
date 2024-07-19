package com.example.buybuy.data.source.local

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.datasource.local.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImp @Inject constructor(
    private val searchDAO: SearchDAO,
    private val favoriteDAO: FavoriteDAO
) : LocalDataSource {
    override suspend fun saveProducts(products: List<ProductDetail>) {
        products.forEach {
            searchDAO.insert(it)
        }

    }

    override suspend fun searchProducts(query: String): List<ProductDetail>? {
        return searchDAO.searchAll(query)
    }

    override suspend fun addToFavorite(product: ProductDetail) {
        favoriteDAO.addToFavorite(product)
    }

    override suspend fun deleteFromFavorite(product: Int) {
        favoriteDAO.deleteFromFavorite(product)
    }

    override suspend fun getAllFavoriteProducts(): List<ProductDetail> =
        favoriteDAO.getFavoriteProducts()

    override suspend fun isProductFavorite(productId: Int): Boolean =
        favoriteDAO.isProductFavorite(productId)


}