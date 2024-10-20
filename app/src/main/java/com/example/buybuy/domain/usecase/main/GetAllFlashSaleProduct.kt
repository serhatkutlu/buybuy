package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class GetAllFlashSaleProduct @Inject constructor(private val repository: MainRepository){
    suspend  operator fun invoke() = repository.getAllFlashSaleProduct()
}