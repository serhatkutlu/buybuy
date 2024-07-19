package com.example.buybuy.domain.usecase.favorite

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(private val repository:MainRepository) {

    suspend  operator fun invoke(productDetail: ProductDetail) =repository.addToFavorite(productDetail)
}