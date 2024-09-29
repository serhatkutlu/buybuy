package com.example.buybuy.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProfileOption
import com.example.buybuy.domain.usecase.login.LogOutUseCase
import com.example.buybuy.enums.ProfileOptionsEnum
import com.example.buybuy.util.Constant.ABOUT
import com.example.buybuy.util.Constant.ADDRESS
import com.example.buybuy.util.Constant.COUPONS
import com.example.buybuy.util.Constant.HELP
import com.example.buybuy.util.Constant.LOGOUT
import com.example.buybuy.util.Constant.ORDER
import com.example.buybuy.util.Constant.PAYMENT
import com.example.buybuy.util.Constant.SETTINGS
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val LogOutUseCase: LogOutUseCase) : ViewModel() {
    private val _profileOptions = MutableStateFlow<Resource<List<ProfileOption>>>(Resource.Empty)
    val profileOptions: StateFlow<Resource<List<ProfileOption>>> = _profileOptions

    private val _logOutState = MutableStateFlow<Resource<Nothing>>(Resource.Empty)
    val logOutState: StateFlow<Resource<Nothing>> = _logOutState

    init {
        initProfileOption()
    }

    private fun initProfileOption() {
        try {
            _profileOptions.value = Resource.Loading()
            listOf(
                ProfileOption(ProfileOptionsEnum.ORDER, R.drawable.nav_order, ORDER),
                ProfileOption(ProfileOptionsEnum.COUPONS, R.drawable.coupon, COUPONS),
                ProfileOption(ProfileOptionsEnum.ADDRESS, R.drawable.location, ADDRESS),
                ProfileOption(ProfileOptionsEnum.PAYMENT, R.drawable.payment, PAYMENT),
                ProfileOption(ProfileOptionsEnum.LOGOUT, R.drawable.nav_logout, LOGOUT),
                ProfileOption(ProfileOptionsEnum.SETTINGS, R.drawable.settings, SETTINGS),
                ProfileOption(ProfileOptionsEnum.HELP, R.drawable.help, HELP),
                ProfileOption(ProfileOptionsEnum.ABOUT, R.drawable.about, ABOUT)
            )
                .let {
                    _profileOptions.value = Resource.Success(it)
                }

        } catch (e: Exception) {
            _profileOptions.value = Resource.Error(e.message.toString())

        }
    }

    fun logOut(){
        viewModelScope.launch {
            LogOutUseCase().collect{
                _logOutState.emit(it)
            }
        }
    }
}