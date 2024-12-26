package com.example.network.di

import com.example.network.factory.RetrofitFactory
import com.example.network.service.product.ProductService
import com.example.nework.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        RetrofitFactory().createRetrofit(BuildConfig.BASE_URL)



    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): ProductService=
        retrofit.create()

}