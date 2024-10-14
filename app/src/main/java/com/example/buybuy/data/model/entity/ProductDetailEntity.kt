package com.example.buybuy.data.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ProductDetail")

data class ProductDetailEntity(
    @PrimaryKey
    val id: Int,
    val brand: String,
    val category: String,
    val color: String,
    val description: String,
    val discount: Int,
    val image: String,
    val model: String,
    val onSale: Boolean,
    val popular: Boolean,
    val price: Int,
    val title: String,
    var star: Float,
    var isFavorite: Boolean,
    var pieceCount: Int
)
