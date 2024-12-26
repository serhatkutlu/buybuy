package com.example.network.service.product

import com.example.nework.dto.product.Category
import com.example.nework.dto.product.Product
import com.example.nework.dto.product.SingleProductData
import com.example.network.endpoints.ProductsEndpoints.GET_SINGLE_PRODUCT
import com.example.network.service.base.BaseService
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService:BaseService {


    @GET(GET_SINGLE_PRODUCT)
    suspend fun getSingleProducts(
        @Path("id") id:Int
    ): Response<SingleProductData>

    @GET("products/category")
    suspend fun getAllCategories(): Response<Category>

    @GET("products/category")
    suspend fun getProductsByCategory(
        @Query("type") type:String
    ): Response<Product>

}