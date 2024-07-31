package com.example.buybuy.domain.usecase.cart

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(private val repository: MainRepository) {
     suspend operator  fun invoke()=repository.clearCart()
}