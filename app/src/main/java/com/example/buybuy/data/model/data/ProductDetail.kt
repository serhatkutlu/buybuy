package com.example.buybuy.data.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProductDetail(
    val brand: String,
    val category: String,
    val color: String?=null,
    val description: String,
    val discount: Int,
    val id: Int,
    val image: String,
    val model: String,
    val onSale: Boolean,
    val popular: Boolean,
    val price: Int,
    val title: String,
    var star: Float?=null

): Parcelable