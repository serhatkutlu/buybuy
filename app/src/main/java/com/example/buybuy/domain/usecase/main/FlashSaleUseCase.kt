package com.example.buybuy.domain.usecase.main

import android.util.Log
import com.example.buybuy.domain.model.data.FlashSaleUiData
import com.example.buybuy.domain.model.mapper.ProductEntityToUiMapper
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject

class FlashSaleUseCase @Inject constructor(
    private val repository: MainRepository,
    private val mapper: ProductEntityToUiMapper
) {

    operator fun invoke(): Flow<Resource<FlashSaleUiData>> = flow {

        try {
            when (val response = repository.getAllFlashSaleProduct()) {
                is Resource.Success -> {

                    val uiData = mapper.mapToModelList(response.data?.data?: emptyList())
                    val flashSaleUiData =
                        FlashSaleUiData(endTime = response.data?.endTime ?:"", data = uiData)

                    emit(Resource.Success(flashSaleUiData))
                }

                is Resource.Error -> {
                    Log.d("serhat", "invoke: ${response.message}")
                    emit(Resource.Error(response.message))
                }

                else -> {}

            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: Constant.UNKNOWN_ERROR))

        }
    }.flowOn(Dispatchers.IO)
}