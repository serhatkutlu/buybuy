package com.example.buybuy.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.domain.model.data.CardInformationData
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.usecase.address.GetAllAddressUseCase
import com.example.buybuy.domain.usecase.cart.GetCartProductsUseCase
import com.example.buybuy.domain.usecase.coupon.GetAllCouponUseCase
import com.example.buybuy.domain.usecase.order.SaveOrderUseCase
import com.example.buybuy.util.Resource
import com.example.buybuy.util.calculateDiscount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val getAllAddressUseCase: GetAllAddressUseCase,
    private val saveOrderUseCase: SaveOrderUseCase,
    private val getAllCouponUseCase: GetAllCouponUseCase
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource<List<ProductDetailUI>>>(Resource.Empty)
    val cartProducts: StateFlow<Resource<List<ProductDetailUI>>> = _cartProducts

    private val _addresses = MutableStateFlow<Resource<List<AddressData>>>(Resource.Empty)
    val addresses: StateFlow<Resource<List<AddressData>>> = _addresses

    private val _totalPrice = MutableStateFlow(0.0f)
    val totalPrice: StateFlow<Float> = _totalPrice

    private val _couponData = MutableStateFlow<Resource<List<CouponData>>>(Resource.Empty)
    val couponData: StateFlow<Resource<List<CouponData>>> = _couponData

    private val _orderState = MutableSharedFlow<Resource<Nothing>>()
    val orderState: SharedFlow<Resource<Nothing>> = _orderState

    var lastAddressId: String? = null
     var lastCardInformationData=CardInformationData()
    var couponId:String?=null

    init {
        getCouponData()
    }

    private fun getCouponData() {
        viewModelScope.launch {
            getAllCouponUseCase.invoke().collect{
                _couponData.emit(it)
            }
        }
    }

    fun getCartData() {
        viewModelScope.launch {
            getCartProductsUseCase.invoke().collect {
                _cartProducts.emit(it)
                if (it is Resource.Success) {
                    calculateTotalPrice(it.data)
                }
            }
        }
    }



    fun getAddressData() {
        viewModelScope.launch {
            getAllAddressUseCase.invoke().collect {
                _addresses.emit(it)
            }
        }
    }

     fun updateTotalPrice(newPrice: Float){
        viewModelScope.launch {
            if (newPrice!=0f){
                _totalPrice.emit(newPrice)

            }else if(cartProducts.value is Resource.Success ){
                calculateTotalPrice((cartProducts.value as Resource.Success<List<ProductDetailUI>>).data)
            }
        }
    }
    private suspend  fun calculateTotalPrice(list: List<ProductDetailUI>?) {
        var totalPrice = 0.0f
        list?.let { products ->
            for (product in products) {
                totalPrice += product.price.calculateDiscount(product.discount)*product.pieceCount
            }
            _totalPrice.emit(totalPrice)
        }

    }

    fun saveOrder(order: List<OrderData>){
        viewModelScope.launch {
            saveOrderUseCase(order,couponId).collect{
                _orderState.emit(it)
            }

        }
    }
}