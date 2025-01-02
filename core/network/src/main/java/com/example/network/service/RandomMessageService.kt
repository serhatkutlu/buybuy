package com.example.network.service

import com.example.network.dto.notification.Notification
import com.example.network.endpoints.NotificationEndPoints
import com.example.network.service.base.BaseService
import retrofit2.Response
import retrofit2.http.GET

interface RandomMessageService:BaseService {

    @GET(NotificationEndPoints.GET_NOTIFICATION)
    suspend fun getRandomMessage(): Response<List<Notification>>
}