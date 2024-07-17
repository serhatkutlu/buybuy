package com.example.buybuy.di

import com.example.buybuy.data.repository.MainRepositoryImp
import com.example.buybuy.data.source.local.SearchDAO
import com.example.buybuy.data.source.local.SearchDataSourceImp
import com.example.buybuy.data.source.remote.RemoteDataSourceImp
import com.example.buybuy.data.source.remote.FakeStoreApi
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideMainRepository(
        remoteDataSource: RemoteDataSource,
        searchDao: SearchDAO
    ): MainRepository = MainRepositoryImp(remoteDataSource,SearchDataSourceImp(searchDao))

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        fakeStoreApi: FakeStoreApi,
        authentcation: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RemoteDataSource = RemoteDataSourceImp(fakeStoreApi, authentcation, firestore)
}