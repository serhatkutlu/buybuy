package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.Address
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface AddressRepository {

    suspend fun saveAddress(address: Address): Flow<Resource<Nothing>>
    suspend fun getAllAddress(): Flow<Resource<List<Address>>>
    suspend fun deleteAddress(addressId: String): Flow<Resource<Nothing>>

}