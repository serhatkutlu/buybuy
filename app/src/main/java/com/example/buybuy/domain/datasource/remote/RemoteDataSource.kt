package com.example.buybuy.domain.datasource.remote

import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.Product
import retrofit2.Response

interface RemoteDataSource {

    suspend fun GetVpBanner():List<String>?
    suspend fun GetAllCategory():Response<Category>

    suspend fun GetProductByCategory(category:String):Response<Product>
}