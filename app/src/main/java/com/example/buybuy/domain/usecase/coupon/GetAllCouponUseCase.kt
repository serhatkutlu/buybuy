package com.example.buybuy.domain.usecase.coupon

 import com.example.buybuy.data.model.data.CouponData
 import com.example.buybuy.domain.repository.CouponRepository
 import com.example.buybuy.util.Resource
 import com.example.buybuy.util.expirationDateCalculate
 import kotlinx.coroutines.flow.Flow
 import kotlinx.coroutines.flow.map
 import javax.inject.Inject

 class GetAllCouponUseCase @Inject constructor(private val repository: CouponRepository) {

     suspend operator fun invoke(): Flow<Resource<List<CouponData>>> = repository.getAllCoupon().map {

         if (it is Resource.Success) {
          val updatedList=it.data?.map {
              if (!it.used && it.expirationDate?.expirationDateCalculate() == true) {
                  it.copy(used = true)
              } else {
                  it
              }
          }
            val sortedList = updatedList?.sortedBy { it.used }
          Resource.Success(sortedList ?: emptyList())
         } else {
             it
         }
     }
 }