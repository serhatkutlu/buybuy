package com.example.buybuy.data.source.local

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.datasource.local.SearchDataSource
import javax.inject.Inject

class SearchDataSourceImp @Inject constructor(private val searchDAO: SearchDAO):SearchDataSource {
    override suspend fun saveProducts(products: List<ProductDetail>) {
        products.forEach {
            searchDAO.insert(it)
        }

    }

    override suspend fun SearchProducts(query: String): List<ProductDetail>? {
        return searchDAO.searchAll(query)
    }


}