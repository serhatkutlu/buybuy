package com.example.buybuy.domain.model.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.buybuy.enums.ProfileOptionsEnum

data class ProfileOption(val type: ProfileOptionsEnum, @DrawableRes val iconResId:Int,@StringRes val title:Int)
