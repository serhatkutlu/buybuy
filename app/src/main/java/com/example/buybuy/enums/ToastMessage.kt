package com.example.buybuy.enums

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.RawRes
import com.example.buybuy.R

import com.example.buybuy.util.showCustomizeToast

enum class ToastMessage(private val message:String, @RawRes private val animUrl:Int, @DrawableRes private val bg:Int, @ColorInt private val color:Int?) {
    SUCCESS("SUCCESS",R.raw.lottie_succesfull, R.drawable.toast_success_bg,R.color.light_green),
    ERROR("ERROR",R.raw.lottie_error,R.drawable.toast_error_bg,R.color.red);
    fun showToast(context: Context) {
        context.showCustomizeToast(message,animUrl,bg,color)
    }
}