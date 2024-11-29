package com.example.buybuy.domain.usecase.cart

import com.example.buybuy.domain.repository.CartRepository
import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(private val repository: CartRepository) {
     suspend operator  fun invoke()=repository.clearCart()
}