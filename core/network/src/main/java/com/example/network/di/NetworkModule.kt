package com.example.network.di

import com.example.network.factory.RetrofitFactory
import com.example.network.service.product.ProductService
import com.example.network.BuildConfig
import com.example.network.service.banners.BannersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    @ProductUrlRetrofit
    fun provideProductRetrofit(): Retrofit =
        RetrofitFactory().createRetrofit(BuildConfig.BASE_URL)


    @Provides
    @Singleton
    @BannerUrlRetrofit
    fun provideBannerRetrofit(): Retrofit =
        RetrofitFactory().createRetrofit(BuildConfig.GIT_URL)



    @Provides
    @Singleton
    fun provideProductService(@ProductUrlRetrofit retrofit: Retrofit): ProductService=
        retrofit.create()

    @Provides
    @Singleton
    fun provideBannersService(@BannerUrlRetrofit retrofit: Retrofit): BannersService =
        retrofit.create()




    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ProductUrlRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BannerUrlRetrofit

}