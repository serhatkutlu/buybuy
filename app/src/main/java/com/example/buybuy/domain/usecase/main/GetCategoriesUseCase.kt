package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val mainRepository: MainRepository) {

    operator fun invoke()= mainRepository.getAllCategory()
}