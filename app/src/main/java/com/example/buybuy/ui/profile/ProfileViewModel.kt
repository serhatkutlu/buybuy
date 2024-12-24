package com.example.buybuy.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.R
import com.example.buybuy.domain.model.data.ProfileOption
import com.example.buybuy.domain.model.data.UserData
import com.example.buybuy.domain.usecase.login.LogOutUseCase
import com.example.buybuy.domain.usecase.main.ClearAllTableUseCase
import com.example.buybuy.domain.usecase.main.GetUserDataUseCase
import com.example.buybuy.enums.ProfileOptionsEnum
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
) : ViewModel() {
    private val _profileOptions = MutableStateFlow<Resource<List<ProfileOption>>>(Resource.Empty)
    val profileOptions: StateFlow<Resource<List<ProfileOption>>> = _profileOptions

    private val _logOutState = MutableStateFlow<Resource<Unit>>(Resource.Empty)
    val logOutState: StateFlow<Resource<Unit>> = _logOutState


    private val _user = MutableStateFlow<Resource<UserData>>(Resource.Empty)
    val user: StateFlow<Resource<UserData>> = _user

    init {
        getUserData()
        initProfileOption()

    }

    private fun getUserData() {
        viewModelScope.launch {
            getUserDataUseCase().collect {
                _user.emit(it)
            }
        }
    }


    private fun initProfileOption() {
        try {
            _profileOptions.value = Resource.Loading()
            listOf(
                ProfileOption(ProfileOptionsEnum.ORDER, R.drawable.nav_order, R.string.order),
                ProfileOption(ProfileOptionsEnum.COUPONS, R.drawable.coupon, R.string.coupons),
                ProfileOption(ProfileOptionsEnum.ADDRESS, R.drawable.location, R.string.address),
                ProfileOption(ProfileOptionsEnum.PAYMENT, R.drawable.payment, R.string.payment,false),
                ProfileOption(ProfileOptionsEnum.SETTINGS, R.drawable.settings, R.string.settings,false),
                ProfileOption(ProfileOptionsEnum.HELP, R.drawable.help, R.string.help,false),
                ProfileOption(ProfileOptionsEnum.ABOUT, R.drawable.about, R.string.about,false),
                ProfileOption(ProfileOptionsEnum.LOGOUT, R.drawable.nav_logout, R.string.logout),

                )
                .also {
                    _profileOptions.value = Resource.Success(it)
                }

        } catch (e: Exception) {
            _profileOptions.value = Resource.Error(e.message.toString())

        }
    }




}