package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.repository.FirebaseRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository){

    operator fun invoke()=firebaseRepository.logOut()
}