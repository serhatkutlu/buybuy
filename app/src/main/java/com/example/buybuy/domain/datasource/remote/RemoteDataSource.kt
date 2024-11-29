package com.example.buybuy.domain.datasource.remote

import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.Product
import com.example.buybuy.data.model.data.SingleProductData
import com.example.buybuy.domain.model.data.SingleBannerData
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getVpBanner():List<String>?
    suspend fun getAllCategory():Response<Category>
    suspend fun getAllSingleBanner():List<SingleBannerData>
    suspend fun getProductByCategory(category:String):Response<Product>
    suspend fun getSingleProduct(id:Int):Response<SingleProductData>
}