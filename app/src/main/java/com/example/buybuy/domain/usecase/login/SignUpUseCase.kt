package com.example.buybuy.domain.usecase.login

import com.example.buybuy.domain.model.User
import com.example.buybuy.domain.repository.FirebaseRepository
import javax.inject.Inject



class SignUpUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {

    operator fun invoke(user: User) =firebaseRepository.createUser(user)


}