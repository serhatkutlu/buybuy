package com.example.buybuy.domain.usecase.cart

import com.example.buybuy.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repository: CartRepository) {
     suspend operator  fun invoke(product:Int)=repository.addToCart(product)
}