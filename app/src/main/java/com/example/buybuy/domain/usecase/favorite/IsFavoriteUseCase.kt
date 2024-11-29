package com.example.buybuy.domain.usecase.favorite

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor( private val repository: MainRepository) {

    //suspend operator fun invoke(id: Int) = repository.isFavorite(id)
}