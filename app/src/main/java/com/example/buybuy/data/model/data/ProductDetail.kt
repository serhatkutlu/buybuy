package com.example.buybuy.data.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "ProductDetail")
data class ProductDetail(
    @PrimaryKey
    val id: Int,
    val brand: String,
    val category: String,
    val color: String?=null,
    val description: String,
    val discount: Int,
    val image: String,
    val model: String,
    val onSale: Boolean,
    val popular: Boolean,
    val price: Int,
    val title: String,
    var star: Float?=null,
    var isFavorite: Boolean=false,
    var cartCount: Int=0

): Parcelable