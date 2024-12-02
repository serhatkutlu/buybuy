package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClearAllTableUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(): Flow<Resource<Unit>> =repository.clearAllTables()
}