package com.example.buybuy.di

import com.example.buybuy.data.repository.Impl.AddressRepositoryImp
import com.example.buybuy.domain.repository.AddressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class addressModule {
    @Binds
    @Singleton
    abstract fun provideAddressRepository(addressRepositoryImp: AddressRepositoryImp): AddressRepository

}