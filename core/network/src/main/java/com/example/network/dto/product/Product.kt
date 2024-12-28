package com.example.network.dto.product

data class Product(
    val message: String,
    val products: List<ProductDetail>,
    val status: String
)