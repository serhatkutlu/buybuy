package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.util.Resource

interface OrdersRepository {
    suspend fun saveOrder(order: List<OrderData>): Boolean
    suspend fun getAllOrder(): Resource<List<OrderData>>
    suspend fun getProductDetail(id: List<Int>): List<ProductDetail>
}