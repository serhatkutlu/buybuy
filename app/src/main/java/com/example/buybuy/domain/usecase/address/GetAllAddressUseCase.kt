package com.example.buybuy.domain.usecase.address

import com.example.buybuy.domain.repository.AddressRepository
import javax.inject.Inject

class GetAllAddressUseCase @Inject constructor(private val repository: AddressRepository) {
     suspend operator  fun invoke()=repository.getAllAddress()
}