package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class PerformSearchUseCase@Inject constructor(private val repository: MainRepository) {

    operator fun invoke(query: String) =repository.searchProduct(query)


}