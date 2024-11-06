package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface CouponRepository {

     fun createRegisterCoupon(couponData:List<CouponData>):Flow<Resource<Nothing>>

     fun getAllCoupon():Flow<Resource<List<CouponData>>>
}