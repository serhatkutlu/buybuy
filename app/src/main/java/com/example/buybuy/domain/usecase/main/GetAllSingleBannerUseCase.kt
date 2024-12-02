package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class GetAllSingleBannerUseCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend operator  fun invoke()=mainRepository.getAllSingleBanner()
}