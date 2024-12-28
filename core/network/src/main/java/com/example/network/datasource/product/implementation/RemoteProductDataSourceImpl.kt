package com.example.network.datasource.product.implementation

import com.example.network.service.product.ProductService
import com.example.network.datasource.product.abstraction.RemoteProductDataSource
import com.example.network.dto.product.SingleProductData
import retrofit2.Response
import javax.inject.Inject

class RemoteProductDataSourceImpl @Inject constructor(private val productService: ProductService):
    RemoteProductDataSource {



    override suspend fun getAllCategory() =productService.getAllCategories()
    override suspend fun getProductByCategory(category: String) = productService.getProductsByCategory(category)
    override suspend fun getSingleProduct(id: Int): Response<SingleProductData> =productService.getSingleProducts(id)
}