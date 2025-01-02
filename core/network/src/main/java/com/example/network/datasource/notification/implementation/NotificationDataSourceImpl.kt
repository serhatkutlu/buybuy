package com.example.network.datasource.notification.implementation

import com.example.network.datasource.notification.abstraction.NotificationDataSource
import com.example.network.dto.notification.Notification
import com.example.network.service.RandomMessageService
import retrofit2.Response
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(private val notificationService:RandomMessageService): NotificationDataSource  {



    override suspend fun getRandomMessage(): Response<List<Notification>> =
        notificationService.getRandomMessage()


}