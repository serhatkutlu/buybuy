package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.repository.FirebaseRepository
import javax.inject.Inject

class CheckUserLoginEmailAndPasswordUseCase @Inject constructor(private val firebaseRepositoryAuth: FirebaseRepository) {


    operator fun invoke(email:String,password:String) =firebaseRepositoryAuth.checkUserEmailAndPassword(email,password)

}