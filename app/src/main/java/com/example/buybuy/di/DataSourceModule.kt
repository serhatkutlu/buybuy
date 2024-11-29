package com.example.buybuy.di

import com.example.buybuy.data.source.local.FlashSaleDAO
import com.example.buybuy.data.source.local.FlashSaleDataSourceImp
import com.example.buybuy.data.source.local.ProductDataSourceImp
import com.example.buybuy.data.source.local.ProductDAO
import com.example.buybuy.data.source.remote.FakeStoreApi
import com.example.buybuy.data.source.remote.RemoteDataSourceImp
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import com.example.buybuy.domain.datasource.local.ProductDataSource
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        fakeStoreApi: FakeStoreApi,
        firestore: FirebaseFirestore
    ): RemoteDataSource = RemoteDataSourceImp(fakeStoreApi, firestore)

    @Provides
    @Singleton
    fun provideLocalDataSource(productDAO: ProductDAO): ProductDataSource =
        ProductDataSourceImp(productDAO)

    @Provides
    @Singleton
    fun provideFlashSaleDataSource(productDAO: FlashSaleDAO): FlashSaleDataSource =FlashSaleDataSourceImp(productDAO)



}