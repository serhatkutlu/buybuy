package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.User
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

     fun createUser(user: User): Flow<Resource<Nothing>>

     fun checkUserLogin():Flow<Boolean>
     fun checkUserEmailAndPassword(email: String, password: String):Flow<Resource<Boolean>>
     fun resetPassword(email: String):Flow<Resource<Nothing>>

}