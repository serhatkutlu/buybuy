package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.mapper.ProductDetailToEntityMapper
import com.example.buybuy.domain.model.mapper.ProductEntityToUiMapper
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductByCategoriesUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val productEntityToUiMapper: ProductEntityToUiMapper,
    private val productDetailToEntityMapper: ProductDetailToEntityMapper

) {
    operator fun invoke(category: String): Flow<Resource<List<ProductDetailUI>>> = flow {
        emit(Resource.Loading())

        mainRepository.getProductByCategory(category).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val productEntity = productDetailToEntityMapper.mapToModelList(resource.data ?: emptyList())
                    mainRepository.saveAllProduct(productEntity)

                    val dbResource = mainRepository.getAllProductFromDbWithCategory(category)

                    when (dbResource) {
                        is Resource.Success -> emit(Resource.Success(productEntityToUiMapper.mapToModelList(dbResource.data ?: emptyList())))
                        is Resource.Loading -> emit(Resource.Loading())
                        is Resource.Error -> emit(Resource.Error(dbResource.message))
                        is Resource.Empty -> emit(Resource.Empty)
                    }
                }
                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
                is Resource.Error -> {
                    emit(Resource.Error(resource.message))
                }
                is Resource.Empty -> {
                    emit(Resource.Empty)
                }
            }
        }
    }


}


