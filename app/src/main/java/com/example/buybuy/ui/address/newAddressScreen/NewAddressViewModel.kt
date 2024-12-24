package com.example.buybuy.ui.address.newAddressScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.domain.usecase.address.SaveAddressUseCase
import com.example.buybuy.domain.usecase.address.UpdateAddressUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewAddressViewModel @Inject constructor(
    private val saveAddressUseCase: SaveAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase
) :
    ViewModel() {
    private val _addressResponse: MutableStateFlow<Resource<Unit>> =
        MutableStateFlow(Resource.Empty)
    val addressResponse: StateFlow<Resource<Unit>> = _addressResponse


    fun savaAddress(addressData: AddressData) {
        viewModelScope.launch(Dispatchers.IO) {
            saveAddressUseCase.invoke(addressData).collect {
                _addressResponse.value = it
            }
        }
    }

    fun updateAddress(addressData: AddressData, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAddressUseCase.invoke(addressData, id).collect {
                _addressResponse.emit(it)
            }

        }
    }
}