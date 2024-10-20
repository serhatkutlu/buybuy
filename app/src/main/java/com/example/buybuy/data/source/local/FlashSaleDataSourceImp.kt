package com.example.buybuy.data.source.local

import com.example.buybuy.data.model.entity.FlashSaleEntity
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import javax.inject.Inject

class FlashSaleDataSourceImp @Inject constructor(val dao: FlashSaleDAO) : FlashSaleDataSource {
    override suspend fun getFlashSale(): FlashSaleEntity =dao.getFlashSale()


    override suspend fun saveFlashSale(flashSale: FlashSaleEntity) {
        dao.insert(flashSale)
    }

    override suspend fun isFlashSaleEmpty(): Boolean =dao.isFlashSaleEmpty()


    override suspend fun clearAll() {
        dao.deleteFlashSale()

    }
}