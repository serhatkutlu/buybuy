package com.example.buybuy.domain.datasource.local

import com.example.buybuy.data.model.entity.FlashSaleEntity

interface FlashSaleDataSource {

    suspend fun getFlashSale(): FlashSaleEntity
    suspend fun saveFlashSale(flashSale:FlashSaleEntity)
    suspend fun isFlashSaleEmpty(): Boolean
    suspend fun clearAll()
}