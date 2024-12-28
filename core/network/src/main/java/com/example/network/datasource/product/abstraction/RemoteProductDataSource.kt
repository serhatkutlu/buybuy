package com.example.network.datasource.product.abstraction

import com.example.network.dto.product.Category
import com.example.network.dto.product.Product
import com.example.network.dto.product.SingleProductData
import retrofit2.Response

interface RemoteProductDataSource {

    suspend fun getAllCategory(): Response<Category>
    suspend fun getProductByCategory(category:String): Response<Product>
    suspend fun getSingleProduct(id:Int): Response<SingleProductData>

}