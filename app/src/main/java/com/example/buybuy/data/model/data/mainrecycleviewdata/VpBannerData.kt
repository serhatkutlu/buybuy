package com.example.buybuy.data.model.data.mainrecycleviewdata

import com.example.buybuy.data.model.enums.ViewType
import com.example.buybuy.domain.model.MainRecycleViewdata

data class VpBannerData(override val type: ViewType?=null,val data:List<String>):MainRecycleViewdata

