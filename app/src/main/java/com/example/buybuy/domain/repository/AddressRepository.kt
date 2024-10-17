package com.example.buybuy.domain.repository

import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

interface AddressRepository {

    suspend fun saveAddress(addressData: AddressData): Flow<Resource<Nothing>>
    suspend fun getAllAddress(): Flow<Resource<List<AddressData>>>
    suspend fun deleteAddress(addressId: String): Flow<Resource<Nothing>>
    suspend fun updateAddress(addressData: AddressData, id: String): Flow<Resource<Nothing>>

}