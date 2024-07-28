package com.example.buybuy.ui.favoritesscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
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
class FragmentFavoritesViewModel @Inject constructor(private val getFavoritesUseCase: GetAllFavoriteUseCase,private val searchFavoritesUseCase: SearchFavoritesUseCase): ViewModel() {


    private val _favoritesState :MutableStateFlow<Resource<List<ProductDetail>>> = MutableStateFlow(Resource.Loading())
    val favoritesState:StateFlow<Resource<List<ProductDetail>>> =_favoritesState
    init {
            getFavorites()
    }

    fun searchOnFavorites(query:String){
        viewModelScope.launch(Dispatchers.IO) {
            searchFavoritesUseCase.invoke(query).collect{
                _favoritesState.emit(it)
            }
        }
    }
    private fun getFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            getFavoritesUseCase.invoke().collect{
                _favoritesState.emit(it)
            }
        }
    }
}