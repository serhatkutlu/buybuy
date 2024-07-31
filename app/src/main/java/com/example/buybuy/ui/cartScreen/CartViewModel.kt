package com.example.buybuy.ui.cartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.usecase.cart.AddToCartUseCase
import com.example.buybuy.domain.usecase.cart.ClearCartUseCase
import com.example.buybuy.domain.usecase.cart.DeleteFromCartUseCase
import com.example.buybuy.domain.usecase.cart.GetCartProductsUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val deleteFromCartUseCase: DeleteFromCartUseCase,
    private val getCartProductsUseCase: GetCartProductsUseCase
) : ViewModel() {
    private val _cartItems:MutableStateFlow<Resource<List<ProductDetail>>> = MutableStateFlow(Resource.Loading())
    val cartItems: StateFlow<Resource<List<ProductDetail>>> = _cartItems
    init {

        getAllCartItems()
    }

    fun getAllCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
        getCartProductsUseCase.invoke().collect{
            _cartItems.emit(it)
        }
        }
    }

    fun deleteFromCart(productDetail: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFromCartUseCase.invoke(productDetail)
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            clearCartUseCase.invoke()
        }
    }

    fun addToCart(productDetail: Int) {
        viewModelScope.launch {
            addToCartUseCase.invoke(productDetail)
        }
    }
}