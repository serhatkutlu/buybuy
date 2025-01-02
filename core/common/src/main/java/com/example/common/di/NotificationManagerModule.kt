package com.example.common.di

import android.content.Context
import com.example.common.NotificationManagerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object NotificationManagerModule {

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerHelper {
        return NotificationManagerHelper(context)
    }


}