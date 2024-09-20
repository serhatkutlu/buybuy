package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(private val repository: FirebaseRepository) {
    operator fun invoke()=repository.getUserData()
}