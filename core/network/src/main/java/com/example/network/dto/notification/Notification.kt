package com.example.network.dto.notification

data class Notification(
    val code: String = "",
    val content: String = "",
    val title: String = "",
    val type: String = "",
    val url: String? = null
)