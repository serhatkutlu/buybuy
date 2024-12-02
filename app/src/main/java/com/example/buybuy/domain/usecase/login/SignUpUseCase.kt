package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.model.data.User
import com.example.buybuy.domain.repository.LoginRepository

import javax.inject.Inject



class SignUpUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(user: User) =loginRepository.createUser(user)


}