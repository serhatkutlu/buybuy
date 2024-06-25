package com.example.buybuy.domain.datasource.remote

import com.example.buybuy.data.model.data.VpBannerData
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun GetVpBanner():Flow<Resource<List<VpBannerData>>>
}