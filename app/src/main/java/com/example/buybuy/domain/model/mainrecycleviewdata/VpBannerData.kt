package com.example.buybuy.domain.model.mainrecycleviewdata

import com.example.buybuy.enums.ViewType
import com.example.buybuy.domain.model.MainRecycleViewdata

data class VpBannerData(override val type: ViewType?=null, val data:List<String>):MainRecycleViewdata

