package com.example.buybuy.domain.datasource.remote

import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.Product
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getVpBanner():List<String>?
    suspend fun getAllCategory():Response<Category>

    suspend fun getProductByCategory(category:String):Response<Product>
}