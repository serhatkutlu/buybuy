package com.example.buybuy.data.model.data

import com.example.buybuy.data.model.entity.ProductDetailEntity


data class FlashSaleData(
    val endTime: String,
    val data: List<ProductDetailEntity>
)
