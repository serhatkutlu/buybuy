package com.example.buybuy.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.enums.ViewType
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.domain.usecase.favorite.AddToFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.domain.usecase.main.GetCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetProductByCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetVpBannerImagesUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
) : ViewModel() {
    private val _vpBannerDataFlow: MutableStateFlow<List<MainRecycleViewTypes>> = MutableStateFlow(
        listOf()
    )
    val vpBannerDataFlow: StateFlow<List<MainRecycleViewTypes>> = _vpBannerDataFlow


    init {
        viewModelScope.launch {
            val getVpBannerImages = async { getVpBannerImages() }
            val getCategories = async { getCategories() }

            val combinedList = (getVpBannerImages.await() + getCategories.await()).toMutableList()
            val divider= MainRecycleViewTypes.Divider
            combinedList.add(divider)
            _vpBannerDataFlow.emit(combinedList)
        }


    }

    fun addToFavorite(productDetail: ProductDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            if (productDetail.isFavorite){
                deleteFavoriteUseCase.invoke(productDetail.id)

            }else{
                addToFavoriteUseCase.invoke(productDetail)

            }
        }
    }

    fun fetchContentForCategory(category: String)=flow {

            getProductByCategoriesUseCase.invoke(category).collect {response->
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
                        (Resource.Error(response.message))
                    }
                    is Resource.Empty -> {}

                }
            }
        }


    private suspend fun getCategories(): List<MainRecycleViewTypes> {
        val result = mutableListOf<MainRecycleViewTypes>()
        getCategoriesUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { rVC ->
                        result.add(rVC)
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
                    it.data?.let { rVC ->
                        result.add(rVC)
                    }

                }

                else -> {}
            }
        }
        return result
    }


}

