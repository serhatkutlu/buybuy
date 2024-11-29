package com.example.buybuy.domain.usecase.coupon

import com.example.buybuy.domain.repository.CouponRepository
import javax.inject.Inject

class DeactivateCouponUseCase @Inject constructor(private val repository:CouponRepository) {
    suspend operator fun invoke(id:String)=repository.deactivateCoupon(id)
}