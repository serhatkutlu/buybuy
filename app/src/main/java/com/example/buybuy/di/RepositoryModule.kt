package com.example.buybuy.di

import com.example.buybuy.data.repository.MainRepositoryImp
import com.example.buybuy.domain.datasource.local.LocalDataSource
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.MainRepository
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
        localDataSource: LocalDataSource
        ): MainRepository= MainRepositoryImp(remoteDataSource,localDataSource)


}