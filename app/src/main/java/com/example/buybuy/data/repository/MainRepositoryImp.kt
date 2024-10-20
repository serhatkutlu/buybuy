package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.model.entity.FlashSaleEntity
import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import com.example.buybuy.domain.model.data.SingleBannerData
import com.example.buybuy.domain.datasource.local.ProductDataSource
import com.example.buybuy.enums.ViewType
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.model.data.FlashSaleUiData
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Constant.NODATAFOUND
import com.example.buybuy.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val productDataSource: ProductDataSource,
    private val flashSaleDataSource: FlashSaleDataSource
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
                    emit(Resource.Success(response.body()?.products?: emptyList<ProductDetail>()))
                } else {
                    emit(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    override fun getAllCategory(): Flow<Resource<MainRecycleViewTypes.RVCategory>> = flow {

        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getAllCategory()
            if (response.isSuccessful) {
                val category =
                    MainRecycleViewTypes.RVCategory(response.body()?.categories, ViewType.CATEGORY)
                emit(Resource.Success(category))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
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

    override suspend fun addToFavorite(productDetail: ProductDetailEntity) {
        productDataSource.addToFavorite(productDetail)
    }

    override suspend fun deleteFromFavorite(productDetail: Int) {
        productDataSource.deleteFromFavorite(productDetail)
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


    override suspend fun getAllFlashSaleProduct(): Resource<FlashSaleEntity>  {


        try {
            if (!flashSaleDataSource.isFlashSaleEmpty()) {
                createFlashSaleItem()
            } else {
                val flashSaleEntity = flashSaleDataSource.getFlashSale()
                val datetime = LocalDateTime.parse(flashSaleEntity.endTime)
                if (datetime >= LocalDateTime.now()) {
                    createFlashSaleItem()
                }
            }
            val flashSaleEntity = flashSaleDataSource.getFlashSale()

            return Resource.Success(flashSaleEntity)

        }catch (e:java.lang.Exception){
            return  Resource.Error(e.localizedMessage ?: Constant.UNKNOWN_ERROR)
        }


    }

    private suspend fun createFlashSaleItem() {
        flashSaleDataSource.clearAll()
        val productDetail = productDataSource.getRandomFlashSaleProducts().shuffled()
        val endTime = LocalDateTime.now().plusDays(1).toString()
        val flashSaleItem = FlashSaleEntity(endTime = endTime, data = productDetail)
        flashSaleDataSource.saveFlashSale(flashSaleItem)

    }

}
