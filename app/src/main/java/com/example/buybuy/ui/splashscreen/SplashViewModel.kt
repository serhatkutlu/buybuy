package com.example.buybuy.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.example.buybuy.domain.usecase.login.CheckUserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(checkUserLoginUseCase: CheckUserLoginUseCase) : ViewModel() {

    val checkUserLogin = checkUserLoginUseCase()
}