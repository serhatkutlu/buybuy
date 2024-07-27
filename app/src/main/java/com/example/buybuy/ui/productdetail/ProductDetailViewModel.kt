package com.example.buybuy.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.usecase.favorite.AddToFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.IsFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deletefavoriteUseCase: DeleteFromFavoriteUseCase,

) : ViewModel() {

    private val _isFavorite: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isFavoriteFlow: MutableStateFlow<Boolean?> = _isFavorite


    fun addToFavorite(productDetail: ProductDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            if (productDetail.isFavorite){
                deletefavoriteUseCase.invoke(productDetail.id)
                _isFavorite.emit(false)

            }else{
                addToFavoriteUseCase.invoke(productDetail)
                _isFavorite.emit(true)
            }
        }
    }


    fun addCart(product: ProductDetail) {

    }

    fun buyNow(product: ProductDetail) {

    }
}