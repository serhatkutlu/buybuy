package com.example.buybuy.domain.usecase.order

import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.domain.repository.OrdersRepository
import com.example.buybuy.domain.usecase.coupon.DeactivateCouponUseCase
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveOrderUseCase @Inject constructor(private val repository: OrdersRepository,private val couponDeactivateUseCase: DeactivateCouponUseCase) {

     operator fun invoke(order: List<OrderData>, couponId: String?) = flow{
         try {
             emit(Resource.Loading())
             val response=repository.saveOrder(order)
             if (response){
                 couponId?.let{
                     couponDeactivateUseCase.invoke(it)
                 }
                 emit(Resource.Success())
             }else{
                 emit(Resource.Error(Constant.UNKNOWN_ERROR))

             }
         }catch (e:Exception){
             emit(Resource.Error(e.localizedMessage?:Constant.UNKNOWN_ERROR))
         }

    }
}