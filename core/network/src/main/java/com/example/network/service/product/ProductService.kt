package com.example.network.service.product

import com.example.network.endpoints.ProductsEndpoints.GET_ALL_CATEGORIES
import com.example.network.endpoints.ProductsEndpoints.GET_PRODUCTS_BY_CATEGORY
import com.example.network.dto.product.Category
import com.example.network.dto.product.Product
import com.example.network.dto.product.SingleProductData
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

    @GET(GET_ALL_CATEGORIES)
    suspend fun getAllCategories(): Response<Category>

    @GET(GET_PRODUCTS_BY_CATEGORY)
    suspend fun getProductsByCategory(
        @Query("type") type:String
    ): Response<Product>

}