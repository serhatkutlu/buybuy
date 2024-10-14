package com.example.buybuy.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.User
import com.example.buybuy.domain.usecase.login.SignUpUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
        val signupUseCase:SignUpUseCase
):ViewModel() {

    private val _user= MutableStateFlow<Resource<Nothing>>(Resource.Empty)
    val user: StateFlow<Resource<Nothing>> = _user

    fun signup(user: User){
        viewModelScope.launch {
            signupUseCase(user).collect{
                _user.emit(it)
            }
        }
    }
}