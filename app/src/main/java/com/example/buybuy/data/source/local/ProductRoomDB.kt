package com.example.buybuy.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.buybuy.data.converters.FlashSaleEntityConverter
import com.example.buybuy.data.model.entity.FlashSaleEntity
import com.example.buybuy.data.model.entity.ProductDetailEntity


@Database(entities = [ProductDetailEntity::class, FlashSaleEntity::class], version = 2)
@TypeConverters(FlashSaleEntityConverter::class)
abstract class ProductRoomDB:RoomDatabase() {
    abstract fun searchDAO(): ProductDAO
    abstract fun flashSaleDAO(): FlashSaleDAO
}