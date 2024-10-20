package com.example.buybuy.domain.model.sealed

import com.example.buybuy.domain.model.data.FlashSaleUiData
import com.example.buybuy.enums.ViewType
import com.example.buybuy.util.Resource
import kotlinx.coroutines.flow.Flow

sealed class MainRecycleViewTypes(val type: ViewType? = null,val ordinal: Int) {
    data class VpBannerData(val vpType: ViewType, val data: List<String>) :
        MainRecycleViewTypes(vpType,1)

    data class RVCategory(val categories: List<String>?, val rvType: ViewType? = null) :
        MainRecycleViewTypes(rvType,3)

    data class SingleBannerDataUi(val image: String, val ordinal1: Int) :
        MainRecycleViewTypes(ViewType.SINGLE_BANNER,ordinal1)

    data class FlashSaleDataUi(val data:FlashSaleUiData, val ordinal1: Int=5) :
        MainRecycleViewTypes(ViewType.FLASH_SALE,ordinal1)

    data object Divider : MainRecycleViewTypes(ViewType.DIVIDER,-1)
}