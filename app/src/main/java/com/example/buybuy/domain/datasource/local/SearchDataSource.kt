package com.example.buybuy.domain.datasource.local

import com.example.buybuy.data.model.data.ProductDetail

interface SearchDataSource {

    suspend fun saveProducts(products: List<ProductDetail>)

    suspend fun SearchProducts(query: String): List<ProductDetail>?
}