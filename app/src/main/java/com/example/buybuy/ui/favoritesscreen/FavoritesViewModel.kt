package com.example.buybuy.ui.favoritesscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.usecase.cart.AddToCartUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.GetAllFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.SearchFavoritesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetAllFavoriteUseCase,
    private val searchFavoritesUseCase: SearchFavoritesUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val deleteFromFavoriteUseCase:DeleteFromFavoriteUseCase
) : ViewModel() {


    private val _favoritesState: MutableStateFlow<Resource<List<ProductDetailUI>>> =
        MutableStateFlow(Resource.Empty)
    val favoritesState: StateFlow<Resource<List<ProductDetailUI>>> = _favoritesState

    init {
        getFavorites()
    }

    fun searchOnFavorites(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchFavoritesUseCase.invoke(query).collect {
                _favoritesState.emit(it)
            }
        }
    }

     fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoritesUseCase.invoke().collect {
                _favoritesState.emit(it)
            }
        }
    }

     fun deleteFromFavorites(productDetail: ProductDetailUI) {
         viewModelScope.launch(Dispatchers.IO) {
             deleteFromFavoriteUseCase(productDetail.id)
             getFavorites()
         }
     }
     suspend fun addToCart(productDetail: ProductDetailUI) =addToCartUseCase(productDetail.id)
}