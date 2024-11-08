package com.example.buybuy.domain.usecase.order

import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.domain.model.data.OrderDataUi
import com.example.buybuy.domain.model.mapper.ProductDetailToProductDetailUi
import com.example.buybuy.domain.repository.OrdersRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import com.example.buybuy.util.formatReadableDate
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetOrdersUseCase @Inject constructor(
    private val repository: OrdersRepository,
    private val mapper: ProductDetailToProductDetailUi
) {

    suspend operator fun invoke() = flow {
        emit(
            Resource.Loading()
        )
        try {

            when (val response = repository.getAllOrder()) {
                is Resource.Success -> {
                    val productDetail = repository.getProductDetail(response.data!!.map { it.data!! })
                    val productDetailUi = mapper.mapToModelList(productDetail)
                    val list = response.data.mapNotNull { orderData: OrderData ->
                        val foundProduct = productDetailUi.find { data -> orderData.data == data.id }
                        foundProduct?.let {
                            OrderDataUi(it, orderData.time?.formatReadableDate()?:"", orderData.piece!!)
                        }

                    }

                    emit(Resource.Success(list))
                }

                else -> {
                    emit(Resource.Error(Constant.UNKNOWN_ERROR))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }


    }
}