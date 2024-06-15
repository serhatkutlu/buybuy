package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.User
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface Firebase {

    suspend fun createUser(user: User): Flow<Resource<Nothing>>
}