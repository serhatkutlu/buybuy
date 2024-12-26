package com.example.buybuy.di

import android.content.Context
import androidx.room.Room
import com.example.buybuy.data.source.local.ProductRoomDB
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {


    @Singleton
    @Provides
    fun provideProductRoomDB(@ApplicationContext context: Context): ProductRoomDB =
        Room.databaseBuilder(context, ProductRoomDB::class.java, "ProductRoom_Database.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideSearchDao(productRoomDB: ProductRoomDB) = productRoomDB.searchDAO()

    @Singleton
    @Provides
    fun provideFlashSaleDao(productRoomDB: ProductRoomDB) = productRoomDB.flashSaleDAO()





}