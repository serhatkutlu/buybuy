package com.example.buybuy.data.repository.Impl

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.data.model.data.Notification
import com.example.buybuy.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authhentication: FirebaseAuth
) {
    suspend fun getRandomMessage(): Notification? {
        try {
            val snapshot = firestore.collection("notifications").get().await()
            val document = snapshot.documents.random()
            val notification = document.toObject(Notification::class.java)
            return notification
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }
    suspend fun createCoupon(coupon: CouponData){
        val uid = authhentication.currentUser?.uid
        uid?.let{
            val userCouponsRef =
                firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                    .collection(Constant.COLLECTION_PATH_COUPON_USER)

            userCouponsRef.document().set(coupon).await()

        }
    }

}
