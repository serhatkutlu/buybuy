package com.example.buybuy.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.domain.usecase.favorite.AddToFavoriteUseCase
import com.example.buybuy.domain.usecase.favorite.DeleteFromFavoriteUseCase
import com.example.buybuy.domain.usecase.main.FlashSaleUseCase
import com.example.buybuy.domain.usecase.main.GetAllSingleBannerUseCase
import com.example.buybuy.domain.usecase.main.GetCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetProductByCategoriesUseCase
import com.example.buybuy.domain.usecase.main.GetVpBannerImagesUseCase
import com.example.buybuy.enums.ViewType
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVpBannerImagesUseCase: GetVpBannerImagesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductByCategoriesUseCase: GetProductByCategoriesUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val getAllSingleBannerUseCase: GetAllSingleBannerUseCase,
    private val createFlashSaleList: FlashSaleUseCase
) : ViewModel() {
    private val _vpBannerDataFlow: MutableSharedFlow<List<MainRecycleViewTypes>> =
        MutableSharedFlow()
    val vpBannerDataFlow: SharedFlow<List<MainRecycleViewTypes>> = _vpBannerDataFlow

    private  val categories =getCategories()
    private val combinedList: MutableList<MainRecycleViewTypes> = mutableListOf()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            val getVpBannerImages = getVpBannerImages()


            fetchContentForCategory().collect { response ->
                response?.let { data ->
                    synchronized(combinedList) {
                        val index =
                            combinedList.indexOfFirst { it is MainRecycleViewTypes.RVCategory }
                        if (index != -1) {
                            combinedList[index] = data
                        } else {
                            combinedList.add(data)
                        }
                        combinedList.sortBy { it.ordinal }
                        _vpBannerDataFlow.tryEmit(combinedList)
                    }
                }
            }
            val getAllSingleBanner = getAllSingleBanner()
            val getFlashSale = createFlashSaleList()
            val divider = MainRecycleViewTypes.Divider
            combinedList.addAll((getVpBannerImages + getAllSingleBanner + getFlashSale + divider).filterNotNull())
            combinedList.sortBy { it.ordinal }
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

     suspend fun fetchContentForCategory(category: String? =null): Flow<MainRecycleViewTypes?> = flow {
        getProductByCategoriesUseCase.invoke(category?:categories[0]).collect { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        emit(
                            MainRecycleViewTypes.RVCategory(
                                categories,
                                Resource.Success(it),
                                ViewType.CATEGORY
                            )
                        )

                    }
                }

                is Resource.Loading -> {

                    emit(
                        MainRecycleViewTypes.RVCategory(
                            categories,
                            Resource.Loading(),
                            ViewType.CATEGORY
                        )
                    )
                }

                is Resource.Error -> {
                    emit(
                        MainRecycleViewTypes.RVCategory(
                            categories,
                            Resource.Error(response.message),
                            ViewType.CATEGORY
                        )
                    )
                }

                else -> {
                    emit(null)
                }
            }
        }
    }.flowOn(Dispatchers.IO)




    private  fun getCategories(): List<String> {
        val category = mutableListOf<String>()
        viewModelScope.launch {
            val response = getCategoriesUseCase().firstOrNull()
            when (response) {
                is Resource.Success -> {
                    response.data?.let { rvc ->
                        category.addAll(rvc)
                    }
                }

                else -> {}
            }
        }
        return category
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


    private suspend fun createFlashSaleList(): MainRecycleViewTypes? {
        return withContext(Dispatchers.IO) {
            var RVType: MainRecycleViewTypes? = null
            createFlashSaleList.invoke().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            RVType = MainRecycleViewTypes.FlashSaleDataUi(it)
                        }
                    }

                    else -> {

                        RVType = null
                    }
                }
            }
            return@withContext RVType
        }
    }


}

