package com.example.buybuy.domain.usecase.favorite

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class GetAllFavoriteUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke()=repository.getAllFavorite()

}