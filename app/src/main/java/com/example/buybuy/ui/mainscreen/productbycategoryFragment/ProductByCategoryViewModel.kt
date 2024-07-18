package com.example.buybuy.ui.mainscreen.productbycategoryFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.usecase.main.GetProductByCategoriesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductByCategoryViewModel @Inject constructor(private val getProductByCategoriesUseCase: GetProductByCategoriesUseCase) :
    ViewModel() {

    private val _productByCategories: MutableStateFlow<Resource<List<ProductDetail>>> =
        MutableStateFlow(Resource.Loading())
    val productByCategories: StateFlow<Resource<List<ProductDetail>>> = _productByCategories
    fun getProductByCategories(category: String) {
        viewModelScope.launch {
            getProductByCategoriesUseCase.invoke(category).collect {
                when (it) {
                    is Resource.Loading -> {
                        _productByCategories.emit(Resource.Loading())
                    }

                    is Resource.Error -> {
                        _productByCategories.emit(Resource.Error(it.message))
                    }

                    is Resource.Success -> {
                        _productByCategories.emit(it)
                    }
                }
            }

        }
    }


}