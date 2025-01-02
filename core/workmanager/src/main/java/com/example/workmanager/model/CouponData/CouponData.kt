package com.example.workmanager.model.CouponData

data class CouponData(
    val id: String?=null,
    val name: String?=null,
    val used: Boolean=true,
    val expirationDate: String?=null,
    val discount: Float?=null
)
