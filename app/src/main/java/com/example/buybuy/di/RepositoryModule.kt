package com.example.buybuy.di

import com.example.buybuy.data.repository.CouponRepositoryImp
import com.example.buybuy.data.repository.LoginRepositoryImp
import com.example.buybuy.data.repository.MainRepositoryImp
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import com.example.buybuy.domain.datasource.local.ProductDataSource
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.CouponRepository
import com.example.buybuy.domain.repository.FirebaseRepository
import com.example.buybuy.domain.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
        flashSaleDataSource: FlashSaleDataSource
        ): MainRepository= MainRepositoryImp(remoteDataSource,productDataSource,flashSaleDataSource)


    @Provides
    @ViewModelScoped
    fun provideFirebaseRepository (firebaseAuth: FirebaseAuth, fireStore: FirebaseFirestore, storage: FirebaseStorage): FirebaseRepository =
        LoginRepositoryImp(firebaseAuth,fireStore,storage )


    @Provides
    @ViewModelScoped
    fun provideCouponsRepository(authentication:FirebaseAuth,fireStore: FirebaseFirestore): CouponRepository = CouponRepositoryImp(authentication,fireStore)
}