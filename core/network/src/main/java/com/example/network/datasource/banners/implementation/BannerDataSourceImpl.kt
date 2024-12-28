package com.example.network.datasource.banners.implementation

import com.example.network.datasource.banners.abstraction.BannerDataSource
import com.example.network.dto.banners.SingleBannerData
import com.example.network.dto.banners.VpBannerData
import com.example.network.service.banners.BannersService
import retrofit2.Response
import javax.inject.Inject

class BannerDataSourceImpl @Inject constructor(private val bannersService: BannersService):
    BannerDataSource {
    override suspend fun getVpBanner(): Response<List<VpBannerData>> {
        return bannersService.getVpBanner()
    }

    override suspend fun getAllSingleBanner(): Response<List<SingleBannerData>> {
        return bannersService.getSingleBanner()
    }
}