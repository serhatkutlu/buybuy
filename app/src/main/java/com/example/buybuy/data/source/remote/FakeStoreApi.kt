package com.example.buybuy.data.source.remote

import com.example.buybuy.data.model.data.Product
import com.example.buybuy.data.model.data.Category
import com.example.buybuy.data.model.data.SingleProductData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FakeStoreApi {
    @GET("products")
    suspend fun getAllProducts():Response<Product>


    @GET("products/{id}")
    suspend fun getSingleProducts(
        @Path("id") id:Int
    ):Response<SingleProductData>

    @GET("products/category")
    suspend fun getAllCategories():Response<Category>

    @GET("products/category")
    suspend fun getProductsByCategory(
        @Query("type") type:String
    ):Response<Product>



}