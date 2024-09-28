package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.datasource.local.LocalDataSource
import com.example.buybuy.domain.model.mainrecycleviewdata.RVCategory
import com.example.buybuy.domain.model.mainrecycleviewdata.VpBannerData
import com.example.buybuy.enums.ViewType
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Constant.NODATAFOUND
import com.example.buybuy.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) :
    MainRepository {
    override fun getVpBannerData(): Flow<Resource<VpBannerData>> = flow {
        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getVpBanner()
            response?.let {
                val banner = VpBannerData(ViewType.VP_BANNER, it)
                emit(Resource.Success(banner))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductByCategory(category: String): Flow<Resource<List<ProductDetail>>> =
        flow {
            emit(Resource.Loading())
            try {

                val response = remoteDataSource.getProductByCategory(category)
                if (response.isSuccessful) {
//                    val products=response.body()?.products?.map {
//                        if(isFavorite(it.id)){
//                            it.copy(isFavorite = true)
//                        }else it
//                    }
                    emit(Resource.Success(response.body()?.products))
                } else {
                    emit(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    override fun getAllCategory(): Flow<Resource<RVCategory>> = flow {

        emit(Resource.Loading())
        try {
            val response = remoteDataSource.getAllCategory()
            if (response.isSuccessful) {
                val category = RVCategory(response.body()?.categories, ViewType.CATEGORY)
                emit(Resource.Success(category))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }


    }.flowOn(Dispatchers.IO)

    override suspend fun saveAllProduct(productDetail: List<ProductDetail>) {
        try {
            localDataSource.saveProducts(productDetail)
        } catch (e: Exception) {
        }
    }

    override fun searchProduct(query: String): Flow<Resource<List<ProductDetail>>> = flow {
        emit(Resource.Loading())
        try {
            val response = localDataSource.searchProducts(query)
            if (response.isNullOrEmpty()) {
                emit(Resource.Error(NODATAFOUND))
            } else {
                emit(Resource.Success(response))

            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addToFavorite(productDetail: ProductDetail) {
        localDataSource.addToFavorite(productDetail)
    }

    override suspend fun deleteFromFavorite(productDetail: Int) {
        localDataSource.deleteFromFavorite(productDetail)
    }

    override fun getAllFavorite(): Flow<Resource<List<ProductDetail>>> = flow{
        emit(Resource.Loading())

        try {
            val response=localDataSource.getAllFavoriteProducts()
            if (response.isEmpty()){
                emit(Resource.Error(NODATAFOUND))
            }else{
                emit(Resource.Success(response))
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun searchFavorites(query: String): Flow<Resource<List<ProductDetail>>> = flow{

        emit(Resource.Loading())

        try {
            val response=localDataSource.searchFavoriteProducts(query)
            if (response.isNullOrEmpty()){
                emit(Resource.Error(NODATAFOUND))
            }else{
                emit(Resource.Success(response))
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
            }
    }

    override fun getCartProducts(): Flow<Resource<List<ProductDetail>>> = flow{
        emit(Resource.Loading())
        try {
            val response=localDataSource.getCartProducts()
            if (response.isEmpty()){
                emit(Resource.Error(NODATAFOUND))
            }else{
                emit(Resource.Success(response))
        }
    }catch (e:Exception){
        emit(Resource.Error(e.message.toString()))
    }

    }



    override suspend fun addToCart(product: Int) {
        localDataSource.addToCart(product)
    }

    override suspend fun reduceProductInCart(product: Int) {
        localDataSource.reduceProductInCart(product)
    }

    override suspend fun deleteProductFromCart(product: Int) {
        localDataSource.deleteProductFromCart(product)
    }

    override suspend fun clearCart() {
        localDataSource.deleteAllProductsFromCart()
    }


    override suspend fun isFavorite(productDetail: Int): Boolean =localDataSource.isProductFavorite(productDetail)

}
