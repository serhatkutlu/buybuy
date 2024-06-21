package com.example.buybuy.di

import com.example.buybuy.data.repository.FirebaseRepositoryImp
import com.example.buybuy.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {


    @Provides
    @Singleton
    fun provideFirebaseAuth()= Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore()= Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseStorage()= Firebase.storage

    @Provides
    @Singleton
    fun provideFirebaseRepository (firebaseAuth: FirebaseAuth, fireStore: FirebaseFirestore, storage: FirebaseStorage): FirebaseRepository =
        FirebaseRepositoryImp(firebaseAuth,fireStore,storage )

}