package com.example.buybuy.data.source.local

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import javax.inject.Inject

class FlashSaleDataSourceImp @Inject constructor(private val dao: FlashSaleDAO) : FlashSaleDataSource {


    override suspend fun getFlashSale(): List<ProductDetailEntity> =dao.getFlashSale()


    override suspend fun createFlashSale() {
        dao.crateFlashSaleItem()
    }

    override suspend fun clearAll() {
        dao.resetFlashSale()

    }
}