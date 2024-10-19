package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface CouponRepository {

    suspend fun createRegisterCoupon(couponData: CouponData):Flow<Resource<Nothing>>

    suspend fun getAllCoupon():Flow<Resource<CouponData>>
}