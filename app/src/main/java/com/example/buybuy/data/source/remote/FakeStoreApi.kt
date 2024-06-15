package com.example.buybuy.data.source.remote

import com.example.buybuy.data.model.data.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FakeStoreApi {

    @GET("products")
    suspend fun getAllProducts():Response<Product>




}