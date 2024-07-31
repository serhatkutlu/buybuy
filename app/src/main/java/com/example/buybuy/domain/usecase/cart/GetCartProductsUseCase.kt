package com.example.buybuy.domain.usecase.cart

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class GetCartProductsUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke()=repository.getCartProducts()
}