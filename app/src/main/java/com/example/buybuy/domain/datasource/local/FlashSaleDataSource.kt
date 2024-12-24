package com.example.buybuy.domain.datasource.local

import com.example.buybuy.data.model.entity.ProductDetailEntity

interface FlashSaleDataSource {

    suspend fun getFlashSale(): List<ProductDetailEntity>
    suspend fun createFlashSale()
    suspend fun clearAll()
}