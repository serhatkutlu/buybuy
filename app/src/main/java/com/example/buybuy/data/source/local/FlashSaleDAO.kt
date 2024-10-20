package com.example.buybuy.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.buybuy.data.model.entity.FlashSaleEntity


@Dao
interface FlashSaleDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(products: FlashSaleEntity)

    @Query("SELECT * FROM FlashSale")
    fun getFlashSale(): FlashSaleEntity

    @Query("DELETE FROM FlashSale")
    fun deleteFlashSale()

    @Query("SELECT EXISTS (SELECT 1 FROM FlashSale)")
    fun isFlashSaleEmpty(): Boolean


}