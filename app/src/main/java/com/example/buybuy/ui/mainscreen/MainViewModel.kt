package com.example.buybuy.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
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
import com.example.buybuy.util.Constant.DEFAULT_CATEGORY
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
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
    private val _mainRvData: MutableSharedFlow<Resource<List<MainRecycleViewTypes>>> =
        MutableSharedFlow()
    val mainRvData: SharedFlow<Resource<List<MainRecycleViewTypes>>> = _mainRvData


    private var categories= listOf<String>()
    private var combinedList: MutableList<MainRecycleViewTypes> = mutableListOf()
    var currentCategory = DEFAULT_CATEGORY
        private set
    var mainContentJob: Job = Job()


    init {
        viewModelScope.launch {
            categories = getCategories()
        }
    }

    fun fetchMainContent() {
        if (mainContentJob.isActive) mainContentJob.cancel()
        mainContentJob = viewModelScope.launch(Dispatchers.IO) {

            combinedList = mutableListOf()
            _mainRvData.emit(Resource.Loading())


            fetchContentForCategory().collect { response ->
                response?.let { data ->
                    val index = combinedList.indexOfFirst { it is MainRecycleViewTypes.RVCategory }
                    if (index != -1) {
                        combinedList[index] = data
                    } else {
                        combinedList.add(data)
                    }
                }
            }
            val vpBannerDeferred = async { getVpBannerImages() }
            val allSingleBannerDeferred = async { getAllSingleBanner() }
            val flashSaleDeferred = async { getFlashSaleList() }

            val getVpBannerImages = vpBannerDeferred.await()
            val getAllSingleBanner = allSingleBannerDeferred.await()
            val getFlashSale = flashSaleDeferred.await()
            val divider = MainRecycleViewTypes.Divider




            combinedList.addAll((getVpBannerImages + getAllSingleBanner + getFlashSale + divider).filterNotNull())
            combinedList.sortBy { it.ordinal }


            _mainRvData.emit(Resource.Success(combinedList))


        }


    }

    private suspend fun getAllSingleBanner(): List<MainRecycleViewTypes> {

        val result = mutableListOf<MainRecycleViewTypes>()

        getAllSingleBannerUseCase.invoke().collect { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { rvb ->
                        val singleBanner = rvb.map {
                            MainRecycleViewTypes.SingleBannerDataUi(it.url, it.ordinal)
                        }
                        result.addAll(singleBanner)

                    }
                }


                else -> {}
            }
        }

        return result
    }

    suspend fun addToFavorite(productDetail: ProductDetailUI): Boolean {
        return withContext(Dispatchers.IO) {
            if (productDetail.isFavorite) {
                deleteFavoriteUseCase(productDetail.id)

            } else {
                addToFavoriteUseCase(productDetail)

            }

        }
    }

    suspend fun fetchContentForCategory(
        category: String? = null,
    ): Flow<MainRecycleViewTypes.RVCategory?> = flow {
        try {
            getProductByCategoriesUseCase.invoke(category ?: currentCategory).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        currentCategory = category ?: currentCategory
                        response.data?.let {
                            val viewType = MainRecycleViewTypes.RVCategory(
                                categories,
                                Resource.Success(it),
                                ViewType.CATEGORY,
                                currentCategory
                            )

                            val index = combinedList.indexOfFirst {
                                it is MainRecycleViewTypes.RVCategory && it.data is Resource.Success

                            }
                            if (index != -1) {
                                combinedList[index] = viewType
                                _mainRvData.emit(Resource.Success(combinedList.toList()))

                            }
                            emit(
                                viewType
                            )
                        }
                    }

                    is Resource.Loading -> {
                        val viewType = MainRecycleViewTypes.RVCategory(
                            categories,
                            Resource.Loading(),
                            ViewType.CATEGORY,
                            currentCategory
                        )

                        emit(viewType)

                    }

                    is Resource.Error -> {
                        emit(
                            MainRecycleViewTypes.RVCategory(
                                categories,
                                Resource.Error(response.message),
                                ViewType.CATEGORY,
                                currentCategory
                            )
                        )
                    }

                    else -> {
                        emit(null)
                    }
                }
            }
        } catch (e: Exception) {

        }

    }.flowOn(Dispatchers.IO)


    private suspend fun getCategories(): List<String> {
        val category = mutableListOf<String>()
        getCategoriesUseCase().collect {
            if (it is Resource.Success) {
                it.data?.let { rvc ->
                    category.addAll(rvc)
                }
            }
        }
        return category
    }


//    private suspend fun getVpBannerImages(): List<MainRecycleViewTypes> {
//        val result = mutableListOf<MainRecycleViewTypes>()
//        getVpBannerImagesUseCase().collect {
//            when (it) {
//                is Resource.Success -> {
//                    it.data?.let { rvb ->
//                        result.add(rvb)
//                    }
//
//                }
//
//                else -> {}
//            }
//        }
//        return result
//    }
private suspend fun getVpBannerImages(): List<MainRecycleViewTypes> {
    val result = mutableListOf<MainRecycleViewTypes>()
    // Akıştaki verileri almak
    getVpBannerImagesUseCase().collect { resource ->
        when (resource) {
            is Resource.Success -> {
                resource.data?.let { rvb ->

                    result.add(rvb)

                }
            }

            else->{}
        }
    }


    return result
}

    suspend fun getFlashSaleList(): MainRecycleViewTypes? {
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

