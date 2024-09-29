package com.example.buybuy.domain.model.sealed

import com.example.buybuy.enums.ViewType

sealed class MainRecycleViewTypes(val type: ViewType? = null,val ordinal: Int) {
    data class VpBannerData(val vpType: ViewType, val data: List<String>) :
        MainRecycleViewTypes(vpType,1)

    data class RVCategory(val categories: List<String>?, val rvType: ViewType? = null) :
        MainRecycleViewTypes(rvType,3)

    data class SingleBannerDataUi(val image: String, val ordinal1: Int) :
        MainRecycleViewTypes(ViewType.SINGLE_BANNER,ordinal1)

    data object Divider : MainRecycleViewTypes(ViewType.DIVIDER,-1)
}