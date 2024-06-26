package com.example.buybuy.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.VpBannerData
import com.example.buybuy.domain.usecase.main.GetProductByCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetVpBannerImagesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVpBannerImagesUseCase: GetVpBannerImagesUseCase,
    private val getProductByCategoriesUseCase: GetProductByCategoriesUseCase
) : ViewModel() {
    private val _vpBannerDataFlow: MutableStateFlow<List<VpBannerData>> = MutableStateFlow(
        listOf()
    )
    val vpBannerDataFlow: StateFlow<List<VpBannerData>> = _vpBannerDataFlow

    init {
        getVpBannerImages()
        //getCategories()

    }

    private fun getCategories() {
        val mutablelist = _vpBannerDataFlow.value.toMutableList()
        var index = 0
        viewModelScope.launch {
            getVpBannerImagesUseCase().collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            if (mutablelist.size > 0) index = 1
                            mutablelist.add(index, it)
                            _vpBannerDataFlow.emit(mutablelist)
                        }
                    }

                    else -> {}
                }

            }

        }
    }

    private fun getVpBannerImages() {
        val mutablelist = _vpBannerDataFlow.value.toMutableList()
        viewModelScope.launch {
            getVpBannerImagesUseCase().collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            mutablelist.add(0, it)
                            _vpBannerDataFlow.emit(mutablelist)
                        }

                    }

                    else -> {}
                }
            }

        }
    }

}