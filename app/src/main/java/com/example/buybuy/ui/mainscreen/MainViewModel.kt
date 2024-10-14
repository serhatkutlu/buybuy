package com.example.buybuy.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.domain.usecase.favorite.AddToFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.domain.usecase.main.GetAllSingleBannerUseCase
import com.example.buybuy.domain.usecase.main.GetCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetProductByCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetVpBannerImagesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVpBannerImagesUseCase: GetVpBannerImagesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductByCategoriesUseCase: GetProductByCategoriesUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val getAllSingleBannerUseCase: GetAllSingleBannerUseCase
) : ViewModel() {
    private val _vpBannerDataFlow: MutableStateFlow<List<MainRecycleViewTypes>> = MutableStateFlow(
        listOf()
    )
    val vpBannerDataFlow: StateFlow<List<MainRecycleViewTypes>> = _vpBannerDataFlow


    init {

        viewModelScope.launch {
            val getVpBannerImages = getVpBannerImages()
            val getCategories = getCategories()
            val getAllSingleBanner = getAllSingleBanner()
            val combinedList =
                (getVpBannerImages + getCategories + getAllSingleBanner).toMutableList()
            val divider = MainRecycleViewTypes.Divider
            combinedList.sortBy { it.ordinal }
            combinedList.add(divider)
            _vpBannerDataFlow.emit(combinedList)
        }


    }

    private suspend fun getAllSingleBanner(): List<MainRecycleViewTypes> {

        val result = mutableListOf<MainRecycleViewTypes>()

        getAllSingleBannerUseCase.invoke().collect { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { rvb ->
                        val singleBanner = rvb.map {
                            MainRecycleViewTypes.SingleBannerDataUi(it.image, it.ordinal)
                        }
                        result.addAll(singleBanner)
                    }
                }

                else -> {}
            }
        }

        return result
    }

    fun addToFavorite(productDetail: ProductDetailUI) {
        viewModelScope.launch(Dispatchers.IO) {
            if (productDetail.isFavorite) {
                deleteFavoriteUseCase.invoke(productDetail.id)

            } else {
                addToFavoriteUseCase.invoke(productDetail)

            }
        }
    }

    fun fetchContentForCategory(category: String) = flow {

        getProductByCategoriesUseCase.invoke(category).collect { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        emit(Resource.Success(it))
                    }
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())

                }

                is Resource.Error -> {
                    emit(Resource.Error(response.message))
                }

                is Resource.Empty -> {emit(Resource.Empty)}

            }
        }
    }


    private suspend fun getCategories(): List<MainRecycleViewTypes> {
        val result = mutableListOf<MainRecycleViewTypes>()
        getCategoriesUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { rvc ->
                        result.add(rvc)
                    }
                }

                else -> {}
            }

        }
        return result
    }

    private suspend fun getVpBannerImages(): List<MainRecycleViewTypes> {
        val result = mutableListOf<MainRecycleViewTypes>()
        getVpBannerImagesUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { rvb ->
                        result.add(rvb)
                    }

                }

                else -> {}
            }
        }
        return result
    }


}

