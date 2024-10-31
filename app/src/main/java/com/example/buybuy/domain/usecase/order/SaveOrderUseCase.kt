package com.example.buybuy.domain.usecase.order

import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.domain.repository.OrdersRepository
import javax.inject.Inject

class SaveOrderUseCase @Inject constructor(private val repository: OrdersRepository) {

    suspend operator fun invoke(order: List<OrderData>) = repository.saveOrder(order)
}