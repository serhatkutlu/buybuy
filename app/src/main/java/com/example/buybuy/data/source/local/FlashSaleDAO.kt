package com.example.buybuy.data.source.local

import androidx.room.Dao

import androidx.room.Query
import com.example.buybuy.data.model.entity.ProductDetailEntity


@Dao
interface FlashSaleDAO{


    @Query("UPDATE productdetail SET isFlashSale = 1 WHERE id IN (SELECT id FROM productdetail ORDER BY RANDOM() LIMIT 10)")
    fun crateFlashSaleItem()

    @Query("SELECT * FROM productdetail Where isFlashSale=1 ")
    fun getFlashSale(): List<ProductDetailEntity>

    @Query("UPDATE productdetail SET isFlashSale = 0")
    fun resetFlashSale()




}