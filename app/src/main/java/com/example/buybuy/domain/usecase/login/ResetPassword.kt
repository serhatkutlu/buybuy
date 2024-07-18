package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.repository.FirebaseRepository
import javax.inject.Inject

class ResetPassword @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(email: String) = firebaseRepository.resetPassword(email)

}