    package com.example.buybuy.data.source.remote.firebase

    import android.Manifest
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.Context
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.net.Uri

    import androidx.core.app.ActivityCompat
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat

    import com.example.buybuy.R
    import com.example.buybuy.data.model.data.CouponData
    import com.example.buybuy.data.repository.impl.FcmRepositoryImpl
    import com.example.buybuy.ui.MainActivity
    import com.google.firebase.messaging.FirebaseMessagingService
    import com.google.firebase.messaging.RemoteMessage
    import dagger.hilt.android.AndroidEntryPoint
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.Job
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    const val FCM_CHANNEL_ID = "fcm_channel"
    const val FCM_CHANNEL_NAME = "FCM"
    const val FCM_TITLE = "firebase_random_message"
    const val COUPON_NAME="BUY"

    @AndroidEntryPoint
    class FcmService: FirebaseMessagingService() {



        @Inject
        lateinit var  repository: FcmRepositoryImpl

        private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)


        override fun onNewToken(token: String) {
            super.onNewToken(token)
        }

        override fun onMessageReceived(message: RemoteMessage) {
            super.onMessageReceived(message)
            message.data.isNotEmpty().let {
                if (message.data.containsValue(FCM_TITLE)){
                    serviceScope.launch {
                        repository.getRandomMessage()?.let{
                            val type=it.type.getNotificationEnum()
                            when(type){
                                NotificationEnum.NEW_COUPON->{
                                    val couponData=createCouponData()
                                    val messageContent=this@FcmService.getString(R.string.fcm_new_coupon_message,couponData.name)
                                    val messageTitle=this@FcmService.getString(R.string.fcm_new_coupon_title)
                                    sendNotification(messageTitle, messageContent,it.url)
                                    repository.createCoupon(couponData)
                                }
                                NotificationEnum.RANDOM_MESSAGE->{
                                    sendNotification(it.title,it.content)
                                }
                                else->{}
                            }

                        }

                    }

                }else{
                sendNotification(message.notification?.title, message.notification?.body)
            }}
        }

        private fun createCouponData():CouponData {
            val discount=(10..50 step 10).toList().random().toFloat()
            val lastDate=java.time.LocalDate.now().plusDays(2).toString()
            val name= COUPON_NAME+discount
            val coupon= CouponData(
                id = null,
                name = name,
                used = false,
                expirationDate = lastDate,
                discount = discount
            )
            return coupon
        }

        private fun sendNotification(title: String?, body: String?,url:String?=null) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = FCM_CHANNEL_ID
            val channelName = FCM_CHANNEL_NAME

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(channel)



            val intent = if (!url.isNullOrEmpty()) {
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            } else {
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )


            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build()



            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            NotificationManagerCompat.from(this).notify(0, notificationBuilder)

        }

        private enum class NotificationEnum{
            NEW_COUPON,RANDOM_MESSAGE
        }
        private fun String.getNotificationEnum(): NotificationEnum? {
            return when (this) {
                "new_coupon" -> NotificationEnum.NEW_COUPON
                "random_message" -> NotificationEnum.RANDOM_MESSAGE
                else -> null
            }
        }
    }
