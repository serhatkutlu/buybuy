package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.repository.LoginRepository
import javax.inject.Inject

class CheckUserLoginEmailAndPasswordUseCase @Inject constructor(private val loginRepositoryAuth: LoginRepository) {


    suspend operator fun invoke(email:String, password:String) =loginRepositoryAuth.checkUserEmailAndPassword(email,password)

}