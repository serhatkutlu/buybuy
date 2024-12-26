package com.example.buybuy.di

import com.example.buybuy.data.repository.impl.AddressRepositoryImpl
import com.example.buybuy.domain.repository.AddressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AddressModule {

    @Binds
    abstract fun bindAddressRepository(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository


}