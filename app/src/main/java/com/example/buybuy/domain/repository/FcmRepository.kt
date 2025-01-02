package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.CouponData
import com.example.network.dto.notification.Notification

interface FcmRepository {

    suspend fun getRandomMessage(): Notification?
    suspend fun createCoupon(coupon:CouponData)
}