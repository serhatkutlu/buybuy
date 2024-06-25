package com.example.buybuy.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.VpBannerData
import com.example.buybuy.domain.usecase.main.GetVpBannerImagesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val getVpBannerImagesUseCase: GetVpBannerImagesUseCase) : ViewModel() {
    private val _vpBannerDataFlow :MutableSharedFlow<Resource<List<VpBannerData>>> = MutableSharedFlow()
    val vpBannerDataFlow :SharedFlow<Resource<List<VpBannerData>>> =_vpBannerDataFlow
    init {
        getVpBannerImages()
    }
    private fun getVpBannerImages() {
        viewModelScope.launch {
            getVpBannerImagesUseCase().collect{
                    _vpBannerDataFlow.emit(it)
            }

        }
    }

}