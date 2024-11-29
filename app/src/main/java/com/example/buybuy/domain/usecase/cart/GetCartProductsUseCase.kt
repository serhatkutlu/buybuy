package com.example.buybuy.domain.usecase.cart

import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.mapper.ProductEntityToUiMapper
import com.example.buybuy.domain.repository.CartRepository
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCartProductsUseCase @Inject constructor(
    private val repository: CartRepository,
    private val mapper: ProductEntityToUiMapper
) {
    operator fun invoke(): Flow<Resource<List<ProductDetailUI>>> {
         return repository.getCartProducts().map {
            when (it) {
                is Resource.Success -> {
                    val data=mapper.mapToModelList(it.data ?: emptyList())
                    Resource.Success(data)
                }
                is Resource.Error -> {
                    Resource.Error(it.message)
                }
                is Resource.Loading -> {
                    Resource.Loading()
            }
                is Resource.Empty -> {
                    Resource.Empty
                }
                }
        }
    }
}