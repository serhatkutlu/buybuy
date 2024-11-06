package com.example.buybuy.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.domain.model.data.User
import com.example.buybuy.domain.usecase.coupon.CreateRegisterCouponUseCase
import com.example.buybuy.domain.usecase.login.SignUpUseCase
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignUpUseCase,
    private val createRegisterCouponUseCase: CreateRegisterCouponUseCase,

    ) : ViewModel() {

    private val _user = MutableStateFlow<Resource<Nothing>>(Resource.Empty)
    val user: StateFlow<Resource<Nothing>> = _user

    fun signup(user: User) {
        viewModelScope.launch {
            signupUseCase(user).collect {
                when (it) {
                    is Resource.Success -> {
                        createRegisterCouponUseCase(createCouponData()).collect{
                            _user.emit(it)
                        }

                    }

                    else -> {
                        _user.emit(it)
                    }

                }


            }
        }
    }


    private fun createCouponData(): List<CouponData> {
        val list= mutableListOf<CouponData>()

        val activeDate = java.time.LocalDate.now().plusMonths(1).toString()
        list.add(CouponData(Constant.COUPON_COD, false, activeDate, Constant.COUPON_DISCOUNT))

        val disabledDate = java.time.LocalDate.now().minusMonths(1).toString()
        list.add(CouponData(Constant.COUPON_COD_DISABLED, false, disabledDate, Constant.COUPON_DISCOUNT_DISABLED))
        return list

    }
}