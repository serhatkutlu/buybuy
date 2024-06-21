package com.example.buybuy.domain.usecase.Login

import com.example.buybuy.domain.repository.FirebaseRepository
import javax.inject.Inject

class CheckUserLoginUseCase @Inject constructor(private val firebaseRepositoryAuth: FirebaseRepository) {


    operator fun invoke() =firebaseRepositoryAuth.checkUserLogin()

}