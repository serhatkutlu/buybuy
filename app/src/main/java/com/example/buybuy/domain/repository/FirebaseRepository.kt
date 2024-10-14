package com.example.buybuy.domain.repository

import com.example.buybuy.domain.model.data.UserData
import com.example.buybuy.domain.model.data.User
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

     fun createUser(user: User): Flow<Resource<Nothing>>

     fun checkUserLogin():Flow<Boolean>
     fun checkUserEmailAndPassword(email: String, password: String):Flow<Resource<Boolean>>
     fun resetPassword(email: String):Flow<Resource<Nothing>>
     fun logOut():Flow<Resource<Nothing>>

     fun getUserData(): Flow<Resource<UserData>>

}