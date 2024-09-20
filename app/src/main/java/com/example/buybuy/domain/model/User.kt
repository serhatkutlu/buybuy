package com.example.buybuy.domain.model

import android.net.Uri

data class User(

    val email:String,
    val name:String,
    val password:String,
    val image:Uri?=null,
)