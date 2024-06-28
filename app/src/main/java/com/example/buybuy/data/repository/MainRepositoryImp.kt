package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.data.model.data.mainrecycleviewdata.RVCategory
import com.example.buybuy.data.model.data.mainrecycleviewdata.VpBannerData
import com.example.buybuy.data.model.enums.ViewType
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.MainRepository
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(private val remoteDataSource: RemoteDataSource):
    MainRepository {
    override  fun getVpBannerData(): Flow<Resource<VpBannerData>> = flow {
        emit(Resource.Loading())
        try {
            val response=remoteDataSource.GetVpBanner()
            response?.let {
                val Banner= VpBannerData(ViewType.vp_banner,it)
                emit(Resource.Success(Banner))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getProductByCategory(category: String): Flow<Resource<List<ProductDetail>>> =flow{
        emit(Resource.Loading())
        try {
            val response=remoteDataSource.GetProductByCategory(category)
                if (response.isSuccessful){
                    emit(Resource.Success(response.body()?.products))
                }else{
                    emit(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    override fun getAllCategory(): Flow<Resource<RVCategory>> =flow{
        emit(Resource.Loading())
        try {
            val response=remoteDataSource.GetAllCategory()
            if (response.isSuccessful){
                val category=RVCategory(response.body()?.categories,ViewType.category)
                emit(Resource.Success(category))
            }else{
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }






}
