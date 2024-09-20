package com.example.buybuy.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.User
import com.example.buybuy.domain.usecase.login.SignUpUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
        val SignupUseCase:SignUpUseCase
):ViewModel() {

    private val _user= MutableStateFlow<Resource<Nothing>>(Resource.Empty)
    val user: StateFlow<Resource<Nothing>> = _user

    fun Signup(user: User){
        viewModelScope.launch {
            SignupUseCase(user).collect{
                _user.emit(it)
            }
        }
    }
}