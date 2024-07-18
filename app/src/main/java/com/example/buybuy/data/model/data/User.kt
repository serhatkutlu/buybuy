package com.example.buybuy.data.model.data

import android.net.Uri

data class User(

    val email:String,
    val name:String,
    val password:String,
    val image:Uri?=null
)