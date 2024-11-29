package com.example.buybuy.di

import com.example.buybuy.data.repository.CartRepositoryImpl
import com.example.buybuy.data.repository.CouponRepositoryImp
import com.example.buybuy.data.repository.LoginRepositoryImp
import com.example.buybuy.data.repository.MainRepositoryImp
import com.example.buybuy.data.repository.OrdersRepositoryImp
import com.example.buybuy.data.source.local.PreferencesHelper
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import com.example.buybuy.domain.datasource.local.ProductDataSource
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.CartRepository
import com.example.buybuy.domain.repository.CouponRepository
import com.example.buybuy.domain.repository.LoginRepository
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.domain.repository.OrdersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {


    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        remoteDataSource: RemoteDataSource,
        productDataSource: ProductDataSource,
        flashSaleDataSource: FlashSaleDataSource,
        preferencesHelper: PreferencesHelper
    ): MainRepository = MainRepositoryImp(
        remoteDataSource,
        productDataSource,
        flashSaleDataSource,
        preferencesHelper
    )

    @Provides
    @ViewModelScoped
    fun provideCartRepository(

        productDataSource: ProductDataSource,

        ): CartRepository = CartRepositoryImpl(
        productDataSource
    )


    @Provides
    @ViewModelScoped
    fun provideLoginRepository(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore,
        storage: FirebaseStorage
    ): LoginRepository =
        LoginRepositoryImp(firebaseAuth, fireStore, storage)


    @Provides
    @ViewModelScoped
    fun provideCouponsRepository(
        authentication: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): CouponRepository = CouponRepositoryImp(authentication, fireStore)

    @Provides
    @ViewModelScoped
    fun provideOrdersRepository(
        authentication: FirebaseAuth,
        fireStore: FirebaseFirestore,
        remoteDataSource: RemoteDataSource
    ): OrdersRepository = OrdersRepositoryImp(fireStore, authentication, remoteDataSource)
}