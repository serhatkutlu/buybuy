package com.example.buybuy.domain.model.data

import androidx.annotation.DrawableRes
import com.example.buybuy.enums.ProfileOptionsEnum

data class ProfileOption(val type: ProfileOptionsEnum, @DrawableRes val iconResId:Int, val title:String)
