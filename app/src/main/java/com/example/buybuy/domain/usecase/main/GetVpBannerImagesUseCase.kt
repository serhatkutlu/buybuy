package com.example.buybuy.domain.usecase.main

import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import javax.inject.Inject

class GetVpBannerImagesUseCase @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    operator fun invoke() =remoteDataSource.GetVpBanner()
}