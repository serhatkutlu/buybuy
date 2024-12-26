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
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImp: RemoteDataSourceImp): RemoteDataSource

    @Binds
    abstract fun bindProductDataSource(productDataSourceImp: ProductDataSourceImp): ProductDataSource

    @Binds
    abstract fun bindFlashSaleDataSource(flashSaleDataSourceImp: FlashSaleDataSourceImp): FlashSaleDataSource
}
