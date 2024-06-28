package com.example.buybuy.data.source.remote

import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.util.Constant.BANNER
import com.example.buybuy.util.Constant.VPBANNER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val apiService: FakeStoreApi,
    private val authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RemoteDataSource {
    override suspend fun GetVpBanner():List<String>?  {
            val response = firestore.collection(BANNER).document(VPBANNER).get().await()
            val a = response.data?.values?.toList()?.map{it.toString()}
        return a
    }

    override suspend fun GetAllCategory() =apiService.getAllCategories()
    override suspend fun GetProductByCategory(category: String) = apiService.getProductsByCategory(category)


}