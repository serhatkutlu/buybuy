package com.example.buybuy.domain.usecase.favorite

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class SearchFavoritesUseCase @Inject constructor(private val mainRepository: MainRepository) {
     operator fun invoke(query: String) = mainRepository.searchFavorites(query)

}