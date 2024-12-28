package com.example.network.service.banners

import com.example.network.service.base.BaseService
import com.example.network.dto.banners.SingleBannerData
import com.example.network.dto.banners.VpBannerData
import com.example.network.endpoints.BannerEndpoints.GET_SINGLE_BANNER
import com.example.network.endpoints.BannerEndpoints.GET_VP_BANNER
import retrofit2.Response
import retrofit2.http.GET



interface BannersService :BaseService{
    @GET(GET_VP_BANNER)
    suspend fun getVpBanner():Response<List<VpBannerData>>

    @GET(GET_SINGLE_BANNER)
    suspend fun getSingleBanner():Response<List<SingleBannerData>>
}