package com.example.buybuy.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.buybuy.data.model.data.ProductDetail


@Database(entities = [ProductDetail::class], version = 1)
abstract class ProductRoomDB:RoomDatabase() {
    abstract fun searchDAO(): ProductDAO
}