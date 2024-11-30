package com.example.buybuy.data.repository.base

import com.example.buybuy.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response

open class BaseRepository(private val dispatcher: CoroutineDispatcher) {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<Resource<T>> {
        return flow {
            emit(Resource.Loading())
            val response = withContext(dispatcher) { apiCall() }
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Empty) // Boş bir yanıt durumunda
            } else {
                emit(Resource.Error(response.message() ?: "Unknown error"))
            }
        }.catch { exception ->
            // `catch` içerisinde `emit` kullanımı güvenli
            emit(Resource.Error(exception.message ?: "Unexpected error", exception))
        }
    }
}
