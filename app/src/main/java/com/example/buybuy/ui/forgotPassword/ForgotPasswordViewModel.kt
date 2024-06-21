package com.example.buybuy.ui.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.usecase.Login.ResetPassword
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val resetPassword: ResetPassword): ViewModel() {

    private val _resetFlow:MutableSharedFlow<Resource<Nothing>> = MutableSharedFlow()
    val resetFlow:SharedFlow<Resource<Nothing>> = _resetFlow
    fun resetPasswordwithEmail(email: String){
        viewModelScope.launch {
            resetPassword.invoke(email).collect{
                _resetFlow.emit(it)
            }
        }
    }
}