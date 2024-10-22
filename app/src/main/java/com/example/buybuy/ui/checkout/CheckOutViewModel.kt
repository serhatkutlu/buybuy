package com.example.buybuy.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.usecase.cart.GetCartProductsUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(private val getCartProductsUseCase: GetCartProductsUseCase):ViewModel() {

    private val _cartProducts= MutableStateFlow<Resource<List<ProductDetailUI>>>(Resource.Empty)
    val cartProducts: StateFlow<Resource<List<ProductDetailUI>>> = _cartProducts
    init {
        getCartData()
    }
    private fun getCartData(){
        viewModelScope.launch {
            getCartProductsUseCase.invoke().collect{
                _cartProducts.emit(it)
            }
        }
    }
}