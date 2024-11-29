package com.example.buybuy.data.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AddressData(
    val id: String?=null,
    val address: String="",
    val addressName: String="",
    val name: String="",
    val surname: String="",
    val phone: String=""
): Parcelable
