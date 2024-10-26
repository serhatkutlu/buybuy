package com.example.buybuy.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.domain.model.data.CardInformationData
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.usecase.address.GetAllAddressUseCase
import com.example.buybuy.domain.usecase.cart.GetCartProductsUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val getAllAddressUseCase: GetAllAddressUseCase
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource<List<ProductDetailUI>>>(Resource.Empty)
    val cartProducts: StateFlow<Resource<List<ProductDetailUI>>> = _cartProducts

    private val _addresses = MutableStateFlow<Resource<List<AddressData>>>(Resource.Empty)
    val addresses: StateFlow<Resource<List<AddressData>>> = _addresses

    var lastAddressId: String? = null
     var lastCardInformationData=CardInformationData()

    init {
        // getAddressData()
    }

     fun getCartData() {
        viewModelScope.launch {
            getCartProductsUseCase.invoke().collect {
                _cartProducts.emit(it)
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
}