package com.example.buybuy.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.buybuy.data.model.entity.ProductDetailEntity


@Database(entities = [ProductDetailEntity::class], version = 1)
abstract class ProductRoomDB:RoomDatabase() {
    abstract fun searchDAO(): ProductDAO
}