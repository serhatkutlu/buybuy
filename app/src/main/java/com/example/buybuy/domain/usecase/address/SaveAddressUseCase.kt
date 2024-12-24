package com.example.buybuy.domain.usecase.address

import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.domain.repository.AddressRepository
import javax.inject.Inject

class SaveAddressUseCase @Inject constructor(private val repository: AddressRepository)  {
    suspend  operator fun invoke(addressData: AddressData) = repository.saveAddress(addressData)

}