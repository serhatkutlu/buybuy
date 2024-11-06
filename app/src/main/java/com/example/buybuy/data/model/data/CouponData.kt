package com.example.buybuy.data.model.data

data class CouponData(
    val name: String?=null,
    val used: Boolean=false,
    val expirationDate: String?=null,
    val discount: Float?=null
)
