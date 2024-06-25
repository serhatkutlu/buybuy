package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.VpBannerData
import com.example.buybuy.data.model.enums.ViewType
import com.example.buybuy.data.source.remote.FakeStoreApi
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.util.Constant.BANNER
import com.example.buybuy.util.Constant.VPBANNER
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val apiService: FakeStoreApi,
    private val authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RemoteDataSource {
    override fun GetVpBanner(): Flow<Resource<List<VpBannerData>>> = flow {
        emit(Resource.Loading())
        try {
            val response = firestore.collection(BANNER).document(VPBANNER).get().await()
            val a = response.data?.values?.toList()?.map{it.toString()}


            a?.let {
                val Banner= listOf(VpBannerData(ViewType.vp_banner,it))
                emit(Resource.Success(Banner))
            }

        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}