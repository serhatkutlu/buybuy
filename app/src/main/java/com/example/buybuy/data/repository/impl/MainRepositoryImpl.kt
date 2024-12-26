package com.example.buybuy.data.repository.impl

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.model.data.FlashSaleData
import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.data.repository.base.BaseRepository
import com.example.buybuy.data.source.local.PreferencesHelper
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import com.example.buybuy.domain.model.data.SingleBannerData
import com.example.buybuy.domain.datasource.local.ProductDataSource
import com.example.buybuy.enums.ViewType
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Constant.NODATAFOUND
import com.example.buybuy.util.Resource
import com.example.buybuy.util.Resource.Empty.transform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val productDataSource: ProductDataSource,
    private val flashSaleDataSource: FlashSaleDataSource,
    private val preferencesHelper: PreferencesHelper,
) :
    MainRepository, BaseRepository(Dispatchers.IO) {
    override suspend fun getVpBannerData(): Flow<Resource<MainRecycleViewTypes.VpBannerData>> =
        safeCall {
            val list = remoteDataSource.getVpBanner()
            MainRecycleViewTypes.VpBannerData(ViewType.VP_BANNER, list ?: listOf())
        }


    override suspend fun getAllSingleBanner(): Flow<Resource<List<SingleBannerData>>> =
        safeCall {
            remoteDataSource.getAllSingleBanner()
        }


    override suspend fun getProductByCategory(category: String): Flow<Resource<List<ProductDetail>>> =
        safeApiCall {
            remoteDataSource.getProductByCategory(category)
        }.map {
            it.transform {
                if (it == null || it.products.isEmpty()) {
                    Resource.Error(NODATAFOUND)
                } else {
                    Resource.Success(it.products)
                }
            }
        }


    override suspend fun getAllCategory(): Flow<Resource<List<String>>> =
        safeApiCall { remoteDataSource.getAllCategory() }.map {
            it.transform {
                if (it == null || it.categories.isEmpty()) {
                    Resource.Error(Constant.UNKNOWN_ERROR)
                } else Resource.Success(it.categories)

            }

        }


    override suspend fun saveAllProduct(productDetail: List<ProductDetailEntity>) {
        try {
            productDataSource.saveProducts(productDetail)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun searchProduct(query: String): Flow<Resource<List<ProductDetailEntity>>> =
        safeCall {
            productDataSource.searchProducts(query)
        }.map {
            it.transform {
                if (it.isNullOrEmpty()) {
                    Resource.Error(NODATAFOUND)
                } else {
                    Resource.Success(it)
                }
            }
        }

    override suspend fun addToFavorite(productDetail: ProductDetailEntity): Boolean {
        try {
            productDataSource.addToFavorite(productDetail)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun deleteFromFavorite(productDetail: Int): Boolean {
        try {
            productDataSource.deleteFromFavorite(productDetail)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun getAllFavorite(): Flow<Resource<List<ProductDetailEntity>>> =
        safeCall {
            productDataSource.getAllFavoriteProducts()
        }.map {
            it.transform {
                if (it.isNullOrEmpty()) {
                    Resource.Error(NODATAFOUND)
                } else {
                    Resource.Success(it)
                }
            }
        }

    //
    override suspend fun searchFavorites(query: String): Flow<Resource<List<ProductDetailEntity>>> =
        safeCall {
            productDataSource.searchFavoriteProducts(query)
        }.map {
            it.transform {
                if (it.isNullOrEmpty()) {
                    Resource.Error(NODATAFOUND)
                } else {
                    Resource.Success(it)
                }

            }
        }

    override suspend fun getAllProductFromDbWithCategory(category: String): Resource<List<ProductDetailEntity>> {
        return try {
            val response = productDataSource.getAllProductsWithCategory(category)
            Resource.Success(response)

        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }


    override suspend fun isFavorite(productDetail: Int): Boolean =
        productDataSource.isProductFavorite(productDetail)


    override suspend fun getAllFlashSaleProduct(): Resource<FlashSaleData> {
        try {
            if (preferencesHelper.getEndTime() == null) {
                createFlashSaleItem()
            } else {
                val datetime = LocalDateTime.parse(preferencesHelper.getEndTime())
                if (datetime <= LocalDateTime.now()) {
                    createFlashSaleItem()
                }
            }

            val flashSaleData = flashSaleDataSource.getFlashSale()
            val endTime = preferencesHelper.getEndTime() ?: ""
            return Resource.Success(FlashSaleData(endTime, flashSaleData))

        } catch (e: java.lang.Exception) {
            return Resource.Error(e.localizedMessage ?: Constant.UNKNOWN_ERROR)
        }


    }

    override suspend fun clearAllTables(): Flow<Resource<Unit>> =
        safeCall{
            productDataSource.clearDao()
            flashSaleDataSource.clearAll()
            preferencesHelper.clearEndTime()

        }



    private suspend fun createFlashSaleItem() {
        flashSaleDataSource.clearAll()
        flashSaleDataSource.createFlashSale()
        preferencesHelper.createEndTime()
    }

}
