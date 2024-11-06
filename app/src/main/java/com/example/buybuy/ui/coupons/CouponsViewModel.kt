package com.example.buybuy.ui.coupons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.domain.usecase.coupon.GetAllCouponUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CouponsViewModel @Inject constructor(private val getAllCouponUseCase:GetAllCouponUseCase) : ViewModel() {

    private val _coupon = MutableStateFlow<Resource<List<CouponData>>>(Resource.Empty)
    val coupon: StateFlow<Resource<List<CouponData>>> = _coupon

    init {
        getAllCoupon()
    }

    private fun getAllCoupon() {
        viewModelScope.launch {
            getAllCouponUseCase().collect {
                _coupon.emit(it)
            }
        }
    }
}