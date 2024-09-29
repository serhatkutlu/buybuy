package com.example.buybuy.util

import androidx.navigation.NavOptions
import com.example.buybuy.R

object NavOptions {


    val navOption1 = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    val navOptions2 = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_up_in)
        .setExitAnim(R.anim.slide_up_out)
        .setPopEnterAnim(R.anim.slide_down_in)
        .setPopExitAnim(R.anim.slide_down_out)
        .build()

    val navOptions3 = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setPopUpTo(R.id.mainFragment, false).build()

    val navOptions4 = NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).setRestoreState(true).build()

}