package com.example.buybuy.di

import android.content.Context
import androidx.room.Room
import com.example.buybuy.data.source.local.ProductRoomDB
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
    fun provideSearchRoomDB(@ApplicationContext context: Context): ProductRoomDB =
        Room.databaseBuilder(context, ProductRoomDB::class.java, "ProductRoom_Database.db").build()

    @Singleton
    @Provides
    fun provideSearchDao(productRoomDB: ProductRoomDB) = productRoomDB.SearchDAO()





}