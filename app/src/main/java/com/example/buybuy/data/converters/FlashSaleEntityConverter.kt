package com.example.buybuy.data.converters

import androidx.room.TypeConverter
import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FlashSaleEntityConverter {

    @TypeConverter
    fun fromProductDetailList(value: List<ProductDetailEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toProductDetailList(value: String): List<ProductDetailEntity> {
        val listType = object : TypeToken<List<ProductDetailEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }
}