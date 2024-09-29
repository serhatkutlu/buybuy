package com.example.buybuy.data.source.remote

import com.example.buybuy.data.model.data.SingleBannerData
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.util.Constant.BANNER
import com.example.buybuy.util.Constant.SINGLEBANNER
import com.example.buybuy.util.Constant.VPBANNER
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val apiService: FakeStoreApi,
    private val firestore: FirebaseFirestore
) : RemoteDataSource {
    override suspend fun getVpBanner():List<String>?  {
            val response = firestore.collection(BANNER).document(VPBANNER).get().await()
            val a = response.data?.values?.toList()?.map{it.toString()}
        return a
    }

    override suspend fun getAllSingleBanner(): List<SingleBannerData> {
        val response=firestore.collection(BANNER).document(BANNER).collection(SINGLEBANNER).get().await()
        return response.documents.map{SingleBannerData(it.data?.get("image").toString(),it.data?.get("ordinal").toString().toInt())}
    }


    override suspend fun getAllCategory() =apiService.getAllCategories()
    override suspend fun getProductByCategory(category: String) = apiService.getProductsByCategory(category)


}