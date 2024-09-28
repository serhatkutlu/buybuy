package com.example.buybuy.ui.profile

import androidx.lifecycle.ViewModel
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProfileOption
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Exception

class ProfileViewModel : ViewModel() {
    private val _profileOptions = MutableStateFlow<Resource<List<ProfileOption>>>(Resource.Empty)
    val profileOptions: StateFlow<Resource<List<ProfileOption>>> = _profileOptions

    init {
        initProfileOption()
    }

    private fun initProfileOption() {
        try {
            _profileOptions.value = Resource.Loading()
            listOf(
                ProfileOption(ProfileOptionsEnum.ORDER.ordinal, R.drawable.nav_order, ORDER),
                ProfileOption(ProfileOptionsEnum.COUPONS.ordinal, R.drawable.coupon, COUPONS),
                ProfileOption(ProfileOptionsEnum.ADDRESS.ordinal, R.drawable.location, ADDRESS),
                ProfileOption(ProfileOptionsEnum.PAYMENT.ordinal, R.drawable.payment, PAYMENT),
                ProfileOption(ProfileOptionsEnum.LOGOUT.ordinal, R.drawable.nav_logout, LOGOUT),
                ProfileOption(ProfileOptionsEnum.SETTINGS.ordinal, R.drawable.settings, SETTINGS),
                ProfileOption(ProfileOptionsEnum.HELP.ordinal, R.drawable.help, HELP),
                ProfileOption(ProfileOptionsEnum.ABOUT.ordinal, R.drawable.about, ABOUT)
            )
                .let {
                    _profileOptions.value = Resource.Success(it)
                }

        } catch (e: Exception) {
            _profileOptions.value = Resource.Error(e.message.toString())

        }
    }
}