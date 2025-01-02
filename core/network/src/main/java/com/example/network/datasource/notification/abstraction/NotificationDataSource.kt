package com.example.network.datasource.notification.abstraction

import com.example.network.dto.notification.Notification
import retrofit2.Response

interface NotificationDataSource {
    val a:Int
        get() = 7
    suspend fun getRandomMessage(): Response<List<Notification>>
}