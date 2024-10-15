package com.example.buybuy.ui.address.newAddressScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.Address
import com.example.buybuy.domain.usecase.address.SaveAddressUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewAddressViewModel @Inject constructor(private val saveAddressUseCase: SaveAddressUseCase) :
    ViewModel() {
    private val _saveAddressResponse: MutableStateFlow<Resource<Nothing>> =
        MutableStateFlow(Resource.Empty)
    val saveAddressResponse: StateFlow<Resource<Nothing>> = _saveAddressResponse
    fun savaAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            saveAddressUseCase.invoke(address).collect {
                _saveAddressResponse.value = it
            }
        }
    }
}