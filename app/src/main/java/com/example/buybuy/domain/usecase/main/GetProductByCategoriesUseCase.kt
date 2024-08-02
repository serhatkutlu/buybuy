package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class GetProductByCategoriesUseCase @Inject constructor(private val mainRepository: MainRepository) {

    operator fun invoke(category: String) =
        mainRepository.getProductByCategory(category).map { resource ->

            when (resource) {
                is Resource.Success -> {
                    val updatedProducts = resource.data?.map {
                        if (mainRepository.isFavorite(it.id)) it.isFavorite=true
                        if (it.star == null ) it.star=generateRandomFloat()
                        it
                    }
                    mainRepository.saveAllProduct(updatedProducts ?: emptyList())
                    Resource.Success(updatedProducts)

                }

                else -> resource
            }

        }

    private fun generateRandomFloat(): Float {
        val min = 0.0f
        val max = 5.0f

        val randomFloat = Random.nextFloat() * (max - min) + min
        val roundedFloat = (Math.round(randomFloat * 2) / 2.0).toFloat()

        return roundedFloat
    }
}


