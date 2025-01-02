package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.common.NotificationManagerHelper
import com.example.network.datasource.notification.abstraction.NotificationDataSource
import com.example.workmanager.model.CouponData.CouponData
import com.example.workmanager.util.Constant
import com.example.workmanager.util.Constant.COUPON_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

import kotlinx.coroutines.tasks.await


@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationManager: NotificationManagerHelper,
    private val notificationDataSource: NotificationDataSource,
    private val firestore: FirebaseFirestore,
    private val authentication: FirebaseAuth
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {


        val response=notificationDataSource.getRandomMessage()
        var couponData:CouponData?=null
        return if (response.isSuccessful) {
            response.body()?.random()?.let {response->
                val isUserExist=authentication.currentUser?.uid.isNullOrBlank()
                val type=response.type.getNotificationEnum()
                if (type==NotificationEnum.NEW_COUPON){
                    if (!isUserExist){
                        couponData=createCoupon()
                    }else return@let

                }
                couponData?.let {
                    notificationManager.sendNotification(
                        title = context.getString(R.string.fcm_new_coupon_title),
                        content =context.getString(R.string.fcm_new_coupon_message,it.name),
                        url = response.url
                    )
                }?: run{
                    notificationManager.sendNotification(
                        title = response.title,
                        content = response.content,
                        url = response.url
                    )
                }


            }

            Result.success()
        } else {

            Result.failure()
        }

    }


     suspend fun createCoupon():CouponData{
         val coupon=createCouponData()
        val uid = authentication.currentUser?.uid
        uid?.let{
            val userCouponsRef =
                firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                    .collection(Constant.COLLECTION_PATH_COUPON_USER)

            userCouponsRef.document().set(coupon).await()

        }
         return coupon
    }


    private fun createCouponData(): CouponData {
        val discount = (10..50 step 10).toList().random().toFloat()
        val lastDate = java.time.LocalDate.now().plusDays(2).toString()
        val name = COUPON_NAME + discount
        val coupon = CouponData(
            id = null,
            name = name,
            used = false,
            expirationDate = lastDate,
            discount = discount
        )
        return coupon
    }

    private enum class NotificationEnum {
        NEW_COUPON, RANDOM_MESSAGE
    }

    private fun String.getNotificationEnum(): NotificationEnum? {
        return when (this) {
            "new_coupon" -> NotificationEnum.NEW_COUPON
            "random_message" -> NotificationEnum.RANDOM_MESSAGE
            else -> null
        }
    }

}
