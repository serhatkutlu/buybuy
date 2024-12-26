package com.example.buybuy.di

import com.example.buybuy.data.repository.impl.CartRepositoryImpl
import com.example.buybuy.data.repository.impl.CouponRepositoryImpl
import com.example.buybuy.data.repository.impl.FcmRepositoryImpl
import com.example.buybuy.data.repository.impl.LoginRepositoryImpl
import com.example.buybuy.data.repository.impl.MainRepositoryImpl
import com.example.buybuy.data.repository.impl.OrdersRepositoryImpl
import com.example.buybuy.domain.repository.CartRepository
import com.example.buybuy.domain.repository.CouponRepository
import com.example.buybuy.domain.repository.FcmRepository
import com.example.buybuy.domain.repository.LoginRepository
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.domain.repository.OrdersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

    @Binds
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

    @Binds
    abstract fun bindLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindCouponRepository(couponRepositoryImpl: CouponRepositoryImpl): CouponRepository

    @Binds
    abstract fun bindOrdersRepository(ordersRepositoryImpl: OrdersRepositoryImpl): OrdersRepository

    @Binds
    abstract fun bindFcmRepository(fcmRepositoryImpl: FcmRepositoryImpl): FcmRepository
}