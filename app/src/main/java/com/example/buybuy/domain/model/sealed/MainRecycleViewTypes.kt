package com.example.buybuy.domain.model.sealed

import com.example.buybuy.enums.ViewType

sealed class MainRecycleViewTypes(val type: ViewType?=null) {
    data class VpBannerData( val vpType: ViewType, val data:List<String>):MainRecycleViewTypes(vpType)

    data class RVCategory(val categories: List<String>?, val rvType: ViewType?=null):
        MainRecycleViewTypes(rvType)

    data object Divider: MainRecycleViewTypes(ViewType.DIVIDER)
}