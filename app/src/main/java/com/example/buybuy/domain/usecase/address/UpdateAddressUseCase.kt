package com.example.buybuy.domain.usecase.address

import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.data.repository.Impl.AddressRepositoryImp
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val repositoryImp: AddressRepositoryImp) {
    suspend operator fun invoke(addressData: AddressData, id: String) =
        repositoryImp.updateAddress(addressData, id)

}