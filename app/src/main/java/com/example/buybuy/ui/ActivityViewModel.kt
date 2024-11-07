package com.example.buybuy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.UserData
import com.example.buybuy.domain.usecase.login.LogOutUseCase
import com.example.buybuy.domain.usecase.main.ClearAllTableUseCase
import com.example.buybuy.domain.usecase.main.GetUserDataUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val clearAllTableUseCase: ClearAllTableUseCase
) : ViewModel() {

    private val _logOutState = MutableStateFlow<Resource<Nothing>>(Resource.Empty)
    val logOutState: StateFlow<Resource<Nothing>> = _logOutState

    private val _user = MutableStateFlow<Resource<UserData>>(Resource.Empty)
    val user: StateFlow<Resource<UserData>> = _user

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            logOutUseCase().collect {
                clearAllTableUseCase()
                _logOutState.emit(it)


            }
        }
    }


    fun getUserData() {
        viewModelScope.launch {
            getUserDataUseCase().collect {
                _user.emit(it)
            }
        }
    }
}