package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.repository.LoginRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(private val repository: LoginRepository) {
    operator fun invoke()=repository.getUserData()
}