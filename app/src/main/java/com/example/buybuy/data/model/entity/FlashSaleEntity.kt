package com.example.buybuy.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FlashSale")
data class FlashSaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val endTime: String,
    val data: List<ProductDetailEntity>
)
