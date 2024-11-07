package com.example.buybuy.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.usecase.cart.AddToCartUseCase
import com.example.buybuy.domain.usecase.cart.ClearCartUseCase
import com.example.buybuy.domain.usecase.favorite.AddToFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val clearCartUseCase: ClearCartUseCase

    ) : ViewModel() {

    private val _isFavorite: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isFavoriteFlow: MutableStateFlow<Boolean?> = _isFavorite

    private val _buyNow: MutableSharedFlow<Resource<Nothing>> = MutableSharedFlow()
     val buyNowFlow: SharedFlow<Resource<Nothing>> = _buyNow

    fun addToFavorite(productDetail: ProductDetailUI) {
        viewModelScope.launch(Dispatchers.IO) {
            if (productDetail.isFavorite){
                deleteFavoriteUseCase.invoke(productDetail.id)
                _isFavorite.emit(false)

            }else{
                addToFavoriteUseCase.invoke(productDetail)
                _isFavorite.emit(true)
            }
        }
    }


    fun addCart(product: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            addToCartUseCase.invoke(product)
        }
    }

    fun buyNow(product: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            clearCartUseCase()
            addToCartUseCase.invoke(product).collect{
                _buyNow.emit(it)
            }

        }
    }
}