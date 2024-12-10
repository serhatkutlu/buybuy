package com.example.buybuy.data.repository.base

import com.example.buybuy.util.Constant
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
                } ?: emit(Resource.Error(Constant.UNKNOWN_ERROR))
            } else {
                emit(Resource.Error(response.message() ?: Constant.UNKNOWN_ERROR))
            }
        }
            .catch { exception ->
            emit(Resource.Error(exception.localizedMessage ?: Constant.UNKNOWN_ERROR))
        }
    }

    protected suspend fun <T> safeCall(
        query: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading())

        try {
            val result = withContext(dispatcher){query()}
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: Constant.UNKNOWN_ERROR))
        }
    }
}
