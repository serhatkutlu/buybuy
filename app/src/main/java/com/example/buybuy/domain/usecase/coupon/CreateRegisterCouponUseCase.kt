package com.example.buybuy.domain.usecase.coupon

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.domain.repository.CouponRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateRegisterCouponUseCase @Inject constructor(private val repository: CouponRepository) {

suspend operator fun invoke(coupon:CouponData): Flow<Resource<Nothing>> =repository.createRegisterCoupon(coupon)
}