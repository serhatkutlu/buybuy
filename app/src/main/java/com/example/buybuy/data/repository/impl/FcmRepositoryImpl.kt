package com.example.buybuy.data.repository.impl

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.data.repository.base.BaseRepository
import com.example.buybuy.domain.repository.FcmRepository
import com.example.buybuy.util.Constant
import com.example.network.datasource.notification.abstraction.NotificationDataSource
import com.example.network.dto.notification.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authentication: FirebaseAuth,
    private val notificationDataSource: NotificationDataSource
):FcmRepository{
    override suspend fun getRandomMessage(): Notification? {
        try {
            val notification=notificationDataSource.getRandomMessage().body()?.get(0)

            return notification
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


    override suspend fun createCoupon(coupon: CouponData){
        val uid = authentication.currentUser?.uid
        uid?.let{
            val userCouponsRef =
                firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                    .collection(Constant.COLLECTION_PATH_COUPON_USER)

            userCouponsRef.document().set(coupon).await()

        }
    }

}
