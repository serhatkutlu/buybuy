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
    private val createRegisterCouponUseCase: CreateRegisterCouponUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<Resource<Nothing>>(Resource.Empty)
    val user: StateFlow<Resource<Nothing>> = _user

    fun signup(user: User) {
        viewModelScope.launch {
            signupUseCase(user).collect {
                when(it){
                    is Resource.Success -> {
                        createRegisterCouponUseCase(createCouponData()).collect{_ ->
                            _user.emit(it)
                        }

                        }
                    else->{
                        _user.emit(it)
                    }

                    }


            }
        }
    }


    private fun createCouponData():CouponData{
        val dateTime=java.time.LocalDateTime.now().plusMonths(1).toString()
        return CouponData(Constant.COUPON_NAME,false,dateTime,0.3f)
    }
}