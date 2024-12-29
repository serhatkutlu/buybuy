package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import com.example.buybuy.util.Resource.Empty.transform
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVpBannerImagesUseCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend operator fun invoke() = mainRepository.getVpBannerData().map { resource ->
        resource.transform {
            val data = MainRecycleViewTypes.VpBannerData(data = it?.map { it.url } ?: emptyList())
            Resource.Success(data)
        }
    }
}