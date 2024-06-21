package com.example.buybuy.domain.usecase.Login

import android.net.Uri
import com.example.buybuy.data.model.data.User
import com.example.buybuy.domain.repository.FirebaseRepository
import javax.inject.Inject



class SignUpUseCase @Inject constructor(val firebaseRepository: FirebaseRepository) {

    operator fun invoke(user: User) =firebaseRepository.createUser(user)


}