package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.repository.LoginRepository
import javax.inject.Inject

class ResetPassword @Inject constructor(private val loginRepository: LoginRepository) {

    operator fun invoke(email: String) = loginRepository.resetPassword(email)

}