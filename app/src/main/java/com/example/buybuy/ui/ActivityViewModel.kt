package com.example.buybuy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.UserData
import com.example.buybuy.domain.usecase.login.LogOutUseCase
import com.example.buybuy.domain.usecase.main.ClearAllTableUseCase
import com.example.buybuy.domain.usecase.main.GetUserDataUseCase
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val clearAllTableUseCase: ClearAllTableUseCase
) : ViewModel() {

    private val _logOutState = MutableStateFlow<Resource<Unit>>(Resource.Empty)
    val logOutState: StateFlow<Resource<Unit>> = _logOutState

    private val _user = MutableStateFlow<Resource<UserData>>(Resource.Empty)
    val user: StateFlow<Resource<UserData>> = _user

    @OptIn(ExperimentalCoroutinesApi::class)
    fun logOut() {
        viewModelScope.launch {
            logOutUseCase()
                .flatMapConcat { logOutResource ->
                    when (logOutResource) {

                        is Resource.Success -> {
                            clearAllTableUseCase()
                        }
                        is Resource.Error -> {
                            flowOf(Resource.Error(Constant.UNKNOWN_ERROR))
                        }
                        else -> {
                            flowOf(Resource.Loading())
                        }
                    }
                }
                .collect { finalResource ->
                    _logOutState.emit(finalResource)
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