package com.example.buybuy.domain.usecase.favorite

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class DeleteFromFavoriteUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(productDetail: Int):Boolean =
        repository.deleteFromFavorite(productDetail)


}