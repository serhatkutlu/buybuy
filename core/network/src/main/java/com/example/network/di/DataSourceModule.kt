package com.example.network.di

import com.example.network.datasource.banners.abstraction.BannerDataSource
import com.example.network.datasource.banners.implementation.BannerDataSourceImpl
import com.example.network.datasource.notification.abstraction.NotificationDataSource
import com.example.network.datasource.notification.implementation.NotificationDataSourceImpl
import com.example.network.datasource.product.abstraction.RemoteProductDataSource
import com.example.network.datasource.product.implementation.RemoteProductDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteProductDataSourceImpl): RemoteProductDataSource

    @Binds
    abstract fun provideBannerDataSource(bannerDataSource: BannerDataSourceImpl): BannerDataSource

  @Binds
    abstract fun provideNotificationDataSource(notificationDataSourceImpl: NotificationDataSourceImpl): NotificationDataSource


}