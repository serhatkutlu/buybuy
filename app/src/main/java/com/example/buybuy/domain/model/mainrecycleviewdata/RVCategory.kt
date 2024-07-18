package com.example.buybuy.domain.model.mainrecycleviewdata

import com.example.buybuy.domain.model.enums.ViewType
import com.example.buybuy.domain.model.MainRecycleViewdata

data class RVCategory(val categories: List<String>?, override val type: ViewType?=null):MainRecycleViewdata
