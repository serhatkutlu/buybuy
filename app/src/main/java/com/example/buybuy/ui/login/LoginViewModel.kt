package com.example.buybuy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.usecase.login.CheckUserLoginEmailAndPasswordUseCase
import com.example.buybuy.domain.usecase.login.CheckUserLoginUseCase
import com.example.buybuy.util.Constant.UNKNOWN_ERROR
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkUserEmailAndPasswordUseCase: CheckUserLoginEmailAndPasswordUseCase
):ViewModel() {




    private val _loginFlow: MutableStateFlow<Resource<Unit>> = MutableStateFlow(Resource.Empty)
    val loginFlow: StateFlow<Resource<Unit>> =_loginFlow


    fun loginEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            checkUserEmailAndPasswordUseCase.invoke(email, password)
                .catch { exception ->
                    _loginFlow.emit(Resource.Error(exception.message ?: UNKNOWN_ERROR))
                }
                .collect {
                    _loginFlow.emit(it)
                }
        }
    }
}