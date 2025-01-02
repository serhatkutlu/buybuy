package com.example.network.endpoints

object ProductsEndpoints {

    private const val PRODUCTS_PATH = "products/"


    const val GET_SINGLE_PRODUCT = PRODUCTS_PATH + "{id}"

    const val GET_ALL_CATEGORIES = PRODUCTS_PATH + "category"

    const val GET_PRODUCTS_BY_CATEGORY = PRODUCTS_PATH + "category"
}