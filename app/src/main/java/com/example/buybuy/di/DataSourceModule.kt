package com.example.buybuy.di

import com.example.buybuy.data.source.local.LocalDataSourceImp
import com.example.buybuy.data.source.local.ProductDAO
import com.example.buybuy.data.source.remote.FakeStoreApi
import com.example.buybuy.data.source.remote.RemoteDataSourceImp
import com.example.buybuy.domain.datasource.local.LocalDataSource
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.google.firebase.auth.FirebaseAuth
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
        authentcation: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RemoteDataSource = RemoteDataSourceImp(fakeStoreApi, authentcation, firestore)

    @Provides
    @Singleton
    fun provideLocalDataSource(productDAO: ProductDAO): LocalDataSource =
        LocalDataSourceImp(productDAO)
}