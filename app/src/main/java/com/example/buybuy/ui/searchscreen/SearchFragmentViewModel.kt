package com.example.buybuy.ui.searchscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.usecase.main.PerformSearchUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(private val performSearchUseCase: PerformSearchUseCase) :
    ViewModel() {

    private val _searchData: MutableStateFlow<Resource<List<ProductDetail>>> =
        MutableStateFlow(Resource.Loading())
    val searchData: MutableStateFlow<Resource<List<ProductDetail>>> = _searchData
    fun PerformSearch(query: String) {

        viewModelScope.launch() {
            performSearchUseCase(query).collect {
                when (it) {
                    is Resource.Loading -> {

                        _searchData.value = Resource.Loading()
                    }

                    is Resource.Success -> {
                        _searchData.value = Resource.Success(it.data)
                    }

                    is Resource.Error -> {
                        _searchData.value = Resource.Error(it.message)
                    }
                }
            }


        }
    }
}