package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.repository.LoginRepository
import javax.inject.Inject

class CheckUserLoginUseCase @Inject constructor(private val loginRepositoryAuth: LoginRepository) {


    operator fun invoke() =loginRepositoryAuth.checkUserLogin()

}