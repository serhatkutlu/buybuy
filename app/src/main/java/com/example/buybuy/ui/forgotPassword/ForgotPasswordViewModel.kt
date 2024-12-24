package com.example.buybuy.ui.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.usecase.login.ResetPassword
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val resetPassword: ResetPassword): ViewModel() {

    private val _resetFlow:MutableStateFlow<Resource<Unit>> = MutableStateFlow(Resource.Empty)
    val resetFlow: StateFlow<Resource<Unit>> = _resetFlow
    fun resetPasswordWithEmail(email: String){
        viewModelScope.launch {
            resetPassword.invoke(email).collect{
                _resetFlow.emit(it)
            }
        }
    }
}