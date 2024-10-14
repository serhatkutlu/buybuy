package com.example.buybuy.domain.model.data

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize



@Parcelize
data class ProductDetailUI(
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
): Parcelable