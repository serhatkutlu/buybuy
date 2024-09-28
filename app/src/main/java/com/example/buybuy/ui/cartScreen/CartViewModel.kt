package com.example.buybuy.ui.cartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.usecase.cart.AddToCartUseCase
import com.example.buybuy.domain.usecase.cart.ClearCartUseCase
import com.example.buybuy.domain.usecase.cart.DeleteProductFromCartUseCase
import com.example.buybuy.domain.usecase.cart.ReduceProductInCartUseCase
import com.example.buybuy.domain.usecase.cart.GetCartProductsUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val reduceProductInCartUseCase: ReduceProductInCartUseCase,
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val deleteFromCartUseCase: DeleteProductFromCartUseCase
) : ViewModel() {
    private val _cartItems: MutableStateFlow<Resource<List<ProductDetail>>> =
        MutableStateFlow(Resource.Empty)
    val cartItems: StateFlow<Resource<List<ProductDetail>>> = _cartItems

    init {

        getAllCartItems()
    }

    private fun getAllCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getCartProductsUseCase.invoke().collect {
                _cartItems.emit(it)
            }
        }
    }

    fun deleteFromCart(productDetail: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            async(Dispatchers.IO) {
                reduceProductInCartUseCase.invoke(productDetail)
            }.await()

            getAllCartItems()
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            async(Dispatchers.IO) {
                clearCartUseCase.invoke()
            }.await()

            getAllCartItems()
        }
    }

    fun deleteProductFromCart(productDetail: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            async(Dispatchers.IO) {
                deleteFromCartUseCase.invoke(productDetail)
            }.await()

            getAllCartItems()
        }
    }

    fun addToCart(productDetail: Int) {
        viewModelScope.launch {
            async(Dispatchers.IO) {
                addToCartUseCase.invoke(productDetail)
            }.await()

            getAllCartItems()
        }
    }
}