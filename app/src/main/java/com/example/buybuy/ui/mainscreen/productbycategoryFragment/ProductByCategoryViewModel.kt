package com.example.buybuy.ui.mainscreen.productbycategoryFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.usecase.favorite.AddToFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.IsFavoriteUseCase
import com.example.buybuy.domain.usecase.main.GetProductByCategoriesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ProductByCategoryViewModel @Inject constructor(
    private val getProductByCategoriesUseCase: GetProductByCategoriesUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFromFavoriteUseCase
) :
    ViewModel() {

    private val _productByCategories: MutableStateFlow<Resource<List<ProductDetail>>> =
        MutableStateFlow(Resource.Loading())
    val productByCategories: StateFlow<Resource<List<ProductDetail>>> = _productByCategories


    fun getProductByCategories(category: String) {
        viewModelScope.launch {
            getProductByCategoriesUseCase.invoke(category).collect {
                _productByCategories.emit(it)
        }

    }
}


fun addToFavorite(productDetail: ProductDetail) {
    viewModelScope.launch(Dispatchers.IO) {
        val value = productByCategories.value
        if (!productDetail.isFavorite) {
            addToFavoriteUseCase.invoke(productDetail)
            when (value) {
                is Resource.Success -> {
                    val updatedList = value.data?.map {
                        if (it.id == productDetail.id) {
                            it.copy(isFavorite = true)
                        } else it
                    }
                    withContext(Dispatchers.Main) {
                        _productByCategories.value = (Resource.Success(value.data))

                    }
                }

                else -> {}
            }
        } else {
            deleteFavoriteUseCase.invoke(productDetail.id)
            when (value) {
                is Resource.Success -> {
                    val updatedList = value.data?.map {
                        if (it.id == productDetail.id) {
                            it.copy(isFavorite = false)
                        } else it
                    }
                    withContext(Dispatchers.Main) {
                        _productByCategories.value = (Resource.Success(value.data))


                    }
                }

                else -> {}
            }
        }
    }

}


}