package com.example.buybuy.domain.model.mainrecycleviewdata

import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.enums.ViewType
import com.example.buybuy.domain.model.MainRecycleViewdata

data class TlAndVpData(
    override val type: ViewType? = ViewType.category,
    val categories: List<String>,
    val products: List<ProductDetail>
) : MainRecycleViewdata
