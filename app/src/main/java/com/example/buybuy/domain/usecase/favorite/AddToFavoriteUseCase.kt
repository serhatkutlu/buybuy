package com.example.buybuy.domain.usecase.favorite

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.mapper.ProductUiToEntityMapper
import com.example.buybuy.domain.repository.MainRepository
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(private val repository:MainRepository,private val productUiToEntityMapper: ProductUiToEntityMapper) {

    suspend  operator fun invoke(productDetail: ProductDetailUI){
        val data=productUiToEntityMapper.mapToModel(productDetail)
        repository.addToFavorite(data)
    }
}