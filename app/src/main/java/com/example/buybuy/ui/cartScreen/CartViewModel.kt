package com.example.buybuy.ui.cartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.usecase.cart.AddToCartUseCase
import com.example.buybuy.domain.usecase.cart.ClearCartUseCase
import com.example.buybuy.domain.usecase.cart.DeleteProductFromCartUseCase
import com.example.buybuy.domain.usecase.cart.ReduceProductInCartUseCase
import com.example.buybuy.domain.usecase.cart.GetCartProductsUseCase
import com.example.buybuy.util.Resource
import com.example.buybuy.util.calculateDiscount
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
    private val _cartItems: MutableStateFlow<Resource<List<ProductDetailUI>>> =
        MutableStateFlow(Resource.Empty)
    val cartItems: StateFlow<Resource<List<ProductDetailUI>>> = _cartItems

    private val _totalPrice: MutableStateFlow<Float> = MutableStateFlow(0.0f)
    val totalPrice: StateFlow<Float> = _totalPrice

    init {

        getAllCartItems()

    }

    private fun getAllCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getCartProductsUseCase.invoke().collect {
                _cartItems.emit(it)
                when (it) {
                    is Resource.Success -> {
                        calculateTotalPrice(it.data)
                    }
                    else -> {_totalPrice.emit(0.0f)}
                }
            }
        }
    }

    fun reduceProduct(productDetail: Int) {
        viewModelScope.launch(Dispatchers.IO) {
                reduceProductInCartUseCase.invoke(productDetail)
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

    fun increase(productDetail: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            addToCartUseCase.invoke(productDetail).collect{
                getAllCartItems()
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
}