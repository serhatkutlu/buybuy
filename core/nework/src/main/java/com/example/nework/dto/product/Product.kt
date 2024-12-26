package com.example.nework.dto.product

data class Product(
    val message: String,
    val products: List<ProductDetail>,
    val status: String
)