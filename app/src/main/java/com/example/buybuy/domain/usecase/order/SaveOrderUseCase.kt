package com.example.buybuy.domain.usecase.order

import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.domain.repository.OrdersRepository
import com.example.buybuy.domain.usecase.coupon.DeactivateCouponUseCase
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveOrderUseCase @Inject constructor(private val repository: OrdersRepository,private val couponDeactivateUseCase: DeactivateCouponUseCase) {

     operator fun invoke(order: List<OrderData>, couponId: String?): Flow<Resource<Unit>> = flow{
         try {
             emit(Resource.Loading())


             val response=couponId?.let{
                 if (couponDeactivateUseCase.invoke(it)){
                     repository.saveOrder(order)
                 }else false
             } ?: run {
                 repository.saveOrder(order)
             }
             if (response){
                 emit(Resource.Success())
             }else emit(Resource.Error(Constant.UNKNOWN_ERROR))
         }catch (e:Exception){
             emit(Resource.Error(e.localizedMessage?:Constant.UNKNOWN_ERROR))
         }

    }
}