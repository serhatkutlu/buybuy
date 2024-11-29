package com.example.buybuy.domain.usecase.favorite

import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.mapper.ProductEntityToUiMapper
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllFavoriteUseCase @Inject constructor(private val repository: MainRepository,private val mapper: ProductEntityToUiMapper) {
    operator fun invoke(): Flow<Resource<List<ProductDetailUI>>> {

        val data = repository.getAllFavorite()
        return data.map {
            when (it) {
                is Resource.Success -> {

                    val mappedData = mapper.mapToModelList(it.data ?: emptyList())
                    Resource.Success(mappedData)
                }

                is Resource.Error -> {
                    Resource.Error(it.message)
                }

                else -> {
                    Resource.Empty
                }
            }
        }

    }

}