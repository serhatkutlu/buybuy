package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.data.model.data.Notification

interface FcmRepository {

    suspend fun getRandomMessage():Notification?
    suspend fun createCoupon(coupon:CouponData)
}