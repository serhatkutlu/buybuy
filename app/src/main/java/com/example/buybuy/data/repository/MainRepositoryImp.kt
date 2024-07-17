package com.example.buybuy.data.repository

import android.util.Log
import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.source.local.SearchDataSourceImp
import com.example.buybuy.domain.datasource.local.SearchDataSource
import com.example.buybuy.domain.model.mainrecycleviewdata.RVCategory
import com.example.buybuy.domain.model.mainrecycleviewdata.VpBannerData
import com.example.buybuy.domain.model.enums.ViewType
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Constant.NODATAFOUND
import com.example.buybuy.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val searchDataSource: SearchDataSource
) :
    MainRepository {
    override fun getVpBannerData(): Flow<Resource<VpBannerData>> = flow {
        emit(Resource.Loading())
        try {
            val response = remoteDataSource.GetVpBanner()
            response?.let {
                val Banner = VpBannerData(ViewType.vp_banner, it)
                emit(Resource.Success(Banner))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductByCategory(category: String): Flow<Resource<List<ProductDetail>>> =
        flow {
            emit(Resource.Loading())
            try {

                val response = remoteDataSource.GetProductByCategory(category)
                if (response.isSuccessful) {
                    saveAllProduct(response.body()?.products!!)
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
            val response = remoteDataSource.GetAllCategory()
            if (response.isSuccessful) {
                val category = RVCategory(response.body()?.categories, ViewType.category)
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
            searchDataSource.saveProducts(productDetail)
        } catch (e: Exception) {
            Log.d("serhat", "saveAllProduct: ${e.message}")
        }
    }

    override fun searchProduct(query: String): Flow<Resource<List<ProductDetail>>> = flow {
        emit(Resource.Loading())
        try {
            val response = searchDataSource.SearchProducts(query)
            if (response.isNullOrEmpty()) {
                emit(Resource.Error(NODATAFOUND))
            } else {
                emit(Resource.Success(response))

            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}
