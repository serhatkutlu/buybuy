package com.example.network.datasource.banners.abstraction

import com.example.network.dto.banners.SingleBannerData
import com.example.network.dto.banners.VpBannerData
import retrofit2.Response

interface BannerDataSource {
    suspend fun getVpBanner():Response<List<VpBannerData>>
    suspend fun getAllSingleBanner():Response<List<SingleBannerData>>
}