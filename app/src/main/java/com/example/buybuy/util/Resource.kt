package com.example.buybuy.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T?=null) : Resource<T>()
    data class Loading(val message: String = "Loading...") : Resource<Nothing>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
    data object Empty:Resource<Nothing>()



}