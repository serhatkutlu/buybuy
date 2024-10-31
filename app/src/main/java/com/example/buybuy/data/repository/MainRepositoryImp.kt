package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.model.data.FlashSaleData
import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.data.model.entity.ProductDetailEntity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val productDataSource: ProductDataSource,
    private val flashSaleDataSource: FlashSaleDataSource,
    private val preferencesHelper: PreferencesHelper

) :
    MainRepository {
    override fun getVpBannerData(): Flow<Resource<MainRecycleViewTypes.VpBannerData>> = flow {
        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getVpBanner()
            response?.let {
                val banner = MainRecycleViewTypes.VpBannerData(ViewType.VP_BANNER, it)
                emit(Resource.Success(banner))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)


    override fun getAllSingleBanner(): Flow<Resource<List<SingleBannerData>>> = flow {
        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getAllSingleBanner()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductByCategory(category: String): Flow<Resource<List<ProductDetail>>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = remoteDataSource.getProductByCategory(category)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()?.products ?: emptyList<ProductDetail>()))
                } else {
                    emit(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    override fun getAllCategory(): Flow<Resource<List<String>>> = flow {
        val response = remoteDataSource.getAllCategory()
        if (response.isSuccessful) {
            val category =
                response.body()?.categories
            emit(Resource.Success(category))
        } else {
            emit(Resource.Error(response.message()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun saveAllProduct(productDetail: List<ProductDetailEntity>) {
        try {
            productDataSource.saveProducts(productDetail)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun searchProduct(query: String): Flow<Resource<List<ProductDetailEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val response = productDataSource.searchProducts(query)
            if (response.isNullOrEmpty()) {
                emit(Resource.Error(NODATAFOUND))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addToFavorite(productDetail: ProductDetailEntity):Boolean {
        try {
            productDataSource.addToFavorite(productDetail)
            return true
        }catch (e:Exception){
            return false
        }
    }

    override suspend fun deleteFromFavorite(productDetail: Int):Boolean {
        try {
            productDataSource.deleteFromFavorite(productDetail)
            return true
        }catch (e:Exception){
            return false
        }
    }

    override fun getAllFavorite(): Flow<Resource<List<ProductDetailEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val response = productDataSource.getAllFavoriteProducts()
            if (response.isEmpty()) {
                emit(Resource.Error(NODATAFOUND))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun searchFavorites(query: String): Flow<Resource<List<ProductDetailEntity>>> = flow {

        emit(Resource.Loading())

        try {
            val response = productDataSource.searchFavoriteProducts(query)
            if (response.isNullOrEmpty()) {
                emit(Resource.Error(NODATAFOUND))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllProductFromDbWithCategory(category: String): Resource<List<ProductDetailEntity>> {
        return try {
            val response = productDataSource.getAllProductsWithCategory(category)
            Resource.Success(response)

        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override fun getCartProducts(): Flow<Resource<List<ProductDetailEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val response = productDataSource.getCartProducts()
            if (response.isEmpty()) {
                emit(Resource.Error(NODATAFOUND))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun addToCart(product: Int) {
        productDataSource.addToCart(product)
    }

    override suspend fun reduceProductInCart(product: Int) {
        productDataSource.reduceProductInCart(product)
    }

    override suspend fun deleteProductFromCart(product: Int) {
        productDataSource.deleteProductFromCart(product)
    }

    override suspend fun clearCart() {
        productDataSource.deleteAllProductsFromCart()
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

    private suspend fun createFlashSaleItem() {
        flashSaleDataSource.clearAll()
        flashSaleDataSource.createFlashSale()
        preferencesHelper.createEndTime()
    }

}
