package com.example.buybuy.ui.address.addressScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.domain.usecase.address.GetAllAddressUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddressViewModel @Inject constructor(private val getAllAddressUseCase: GetAllAddressUseCase): ViewModel() {

    init {
        getAllAddress()
    }

    private val _getAllAddressResponse: MutableStateFlow<Resource<List<AddressData>>> =
        MutableStateFlow(Resource.Empty)
    val getAllAddressResponse: StateFlow<Resource<List<AddressData>>> = _getAllAddressResponse

     fun getAllAddress(){
        viewModelScope.launch() {
            getAllAddressUseCase.invoke().collect{
                _getAllAddressResponse.value=it

            }

        }
    }


}