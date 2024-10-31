package com.example.buybuy.util

import androidx.navigation.NavOptions
import com.example.buybuy.R

object NavOptions {


    val rightAnim = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    val leftAnim= NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_left)
        .setExitAnim(R.anim.slide_out_right)
        .setPopEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_left)
        .build()

    val upAnim = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_up_in)
        .setExitAnim(R.anim.slide_up_out)
        .setPopEnterAnim(R.anim.slide_down_in)
        .setPopExitAnim(R.anim.slide_down_out)
        .build()

    val navOptions3 = NavOptions.Builder()
        .setPopUpTo(R.id.mainFragment, true)
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    val navOptions4 = NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).setRestoreState(true).build()

}