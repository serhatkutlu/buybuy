package com.example.buybuy.ui.orderscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.domain.model.data.OrderDataUi
import com.example.buybuy.domain.usecase.order.GetOrdersUseCase
import com.example.buybuy.domain.usecase.order.SaveOrderUseCase
import com.example.buybuy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    init {
        getOrders()
    }
    private val _orders:MutableStateFlow<Resource<List<OrderDataUi>>> = MutableStateFlow(Resource.Empty)
    val orders: StateFlow<Resource<List<OrderDataUi>>> = _orders


    private  fun getOrders(){
        viewModelScope.launch {
            getOrdersUseCase().collect{
                _orders.value=it
            }
        }

    }

}