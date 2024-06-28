package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class GetProductByCategoriesUseCase @Inject constructor(private val mainRepository: MainRepository) {

    operator fun invoke(category: String) = mainRepository.getProductByCategory(category)
}