package com.example.buybuy.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.enums.ViewType
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.domain.usecase.main.GetCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetVpBannerImagesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVpBannerImagesUseCase: GetVpBannerImagesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _vpBannerDataFlow: MutableStateFlow<List<MainRecycleViewdata>> = MutableStateFlow(
        listOf())
    val vpBannerDataFlow: StateFlow<List<MainRecycleViewdata>> = _vpBannerDataFlow

    init {
        viewModelScope.launch {
            val getVpBannerImages = async { getVpBannerImages() }
            val getCategories = async { getCategories() }

            val combinedList =( getVpBannerImages.await() + getCategories.await()).toMutableList()
            val Divider: MainRecycleViewdata=object :MainRecycleViewdata{
                override val type: ViewType?
                    get() = ViewType.divider
            }
            combinedList.add(Divider)
            _vpBannerDataFlow.emit(combinedList)
        }


    }

    suspend fun getCategories(): List<MainRecycleViewdata> {
        val result = mutableListOf<MainRecycleViewdata>()
        getCategoriesUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { RVC ->
                        result.add(RVC)
                    }
                }

                else -> {}
            }

        }
        return result
    }

    suspend private fun getVpBannerImages(): List<MainRecycleViewdata> {
        val result = mutableListOf<MainRecycleViewdata>()
        getVpBannerImagesUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { RVC ->
                        result.add(RVC)
                    }

                }

                else -> {}
            }
        }
        return result
    }
}

