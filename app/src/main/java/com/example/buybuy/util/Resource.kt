package com.example.buybuy.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T? = null) : Resource<T>()
    data class Loading(val message: String = "Loading...") : Resource<Nothing>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
    data object Empty : Resource<Nothing>()




    fun <T,R> Resource<T>.transform( transform: (T?) -> Resource<R>): Resource<R> {
        return when (this) {
            is Resource.Success -> {
                transform(this.data)
            }
            is Resource.Error -> this // Hata durumunda aynen döndürüyoruz
            is Resource.Loading -> this // Yükleniyor durumunda aynen döndürüyoruz
            is Resource.Empty -> this // Boş durumunda aynen döndürüyoruz
        }
    }
}
