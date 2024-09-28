package com.example.buybuy.ui.searchscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.usecase.favorite.AddToFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.domain.usecase.main.PerformSearchUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val performSearchUseCase: PerformSearchUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deletefavoriteUseCase: DeleteFromFavoriteUseCase,
) :
    ViewModel() {

    private var _searchData: MutableStateFlow<Resource<List<ProductDetail>>> =
        MutableStateFlow(Resource.Empty)
    val searchData: StateFlow<Resource<List<ProductDetail>>> = _searchData
    fun performSearch(query: String) {

        viewModelScope.launch {
            performSearchUseCase(query).collect {
                _searchData.emit(it)

            }


        }
    }

    fun addToFavorite(productDetail: ProductDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            if (productDetail.isFavorite) {
                deletefavoriteUseCase.invoke(productDetail.id)

            } else {
                addToFavoriteUseCase.invoke(productDetail)

            }
        }
    }
}