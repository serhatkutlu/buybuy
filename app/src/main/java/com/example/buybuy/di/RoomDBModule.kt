package com.example.buybuy.di

import android.content.Context
import androidx.room.Room
import com.example.buybuy.data.source.local.FavoriteDAO
import com.example.buybuy.data.source.local.FavoriteRoomDB
import com.example.buybuy.data.source.local.SearchRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {


    @Singleton
    @Provides
    fun provideSearchRoomDB(@ApplicationContext context: Context): SearchRoomDB =
        Room.databaseBuilder(context, SearchRoomDB::class.java, "SearchRoom_Database.db").build()

    @Singleton
    @Provides
    fun provideSearchDao(searchRoomDB: SearchRoomDB) = searchRoomDB.SearchDAO()


    @Singleton
    @Provides
    fun provideFavoriteRoomDB(@ApplicationContext context: Context): FavoriteRoomDB =
        Room.databaseBuilder(context, FavoriteRoomDB::class.java, "FavoriteRoom_Database.db")
            .build()


    @Singleton
    @Provides
    fun provideFavoriteDao(favoriteRoomDB: FavoriteRoomDB): FavoriteDAO =
        favoriteRoomDB.favoriteDao()

}