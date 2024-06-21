package com.example.buybuy.ui.login

import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.R
import com.example.buybuy.domain.usecase.Login.CheckUserLoginEmailAndPasswordUseCase
import com.example.buybuy.domain.usecase.Login.CheckUserLoginUseCase
import com.example.buybuy.util.Constant.UNKNOWN_ERROR
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkUserLoginUseCase: CheckUserLoginUseCase,
    private val checkUserEmailAndPasswordUseCase: CheckUserLoginEmailAndPasswordUseCase
):ViewModel() {


    val checkUserLogin = checkUserLoginUseCase()

    private val _loginflow: MutableSharedFlow<Resource<Boolean>> = MutableSharedFlow()
    val loginflow:SharedFlow<Resource<Boolean>> =_loginflow


    fun loginEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            checkUserEmailAndPasswordUseCase.invoke(email, password)
                .catch { exception ->
                    _loginflow.emit(Resource.Error(exception.message ?: UNKNOWN_ERROR))
                }
                .collect {
                    _loginflow.emit(it)
                }
        }
    }
}