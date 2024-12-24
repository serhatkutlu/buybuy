package com.example.buybuy.domain.repository

import com.example.buybuy.domain.model.data.UserData
import com.example.buybuy.domain.model.data.User
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

     suspend fun createUser(user: User): Flow<Resource<Unit>>

     suspend fun checkUserLogin():Flow<Resource<Boolean>>
     suspend fun checkUserEmailAndPassword(email: String, password: String):Flow<Resource<Unit>>
     suspend fun resetPassword(email: String):Flow<Resource<Unit>>
     suspend fun logOut(): Flow<Resource<Unit>>

     suspend fun getUserData(): Flow<Resource<UserData>>


}