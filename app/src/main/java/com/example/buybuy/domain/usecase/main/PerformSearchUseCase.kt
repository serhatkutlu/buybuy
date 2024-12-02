package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.mapper.ProductEntityToUiMapper
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PerformSearchUseCase @Inject constructor(
    private val repository: MainRepository,
    private val mapper: ProductEntityToUiMapper
) {

    suspend operator fun invoke(query: String): Flow<Resource<List<ProductDetailUI>>> {
        return repository.searchProduct(query).map {
            when (it) {
                is Resource.Success -> {
                    val data = mapper.mapToModelList(it.data ?: emptyList())
                    Resource.Success(data)
                }

                is Resource.Error -> {
                    it
                }

                is Resource.Loading -> {
                   it
                }

                is Resource.Empty -> {
                    it
                }
            }
        }
    }


}