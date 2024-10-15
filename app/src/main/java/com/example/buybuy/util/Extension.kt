package com.example.buybuy.util

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.RawRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.buybuy.R
import com.example.buybuy.databinding.LayoutToastMessageBinding
import com.example.buybuy.util.Constant.PREFS_NAME
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private var job: kotlinx.coroutines.Job? = null

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
fun View.invisible() {
    visibility = View.INVISIBLE
}

fun TextInputEditText.checkEmail(error: String): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    return if (android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()) {
        textInputLayout.isErrorEnabled = false
        true
    } else {
        textInputLayout.error = error
        false
    }

}
fun TextInputEditText.checkNullorEmpty(error: String): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    return if (text.toString().isNotEmpty() && text.toString().isNotBlank()) {
        textInputLayout.isErrorEnabled = false
        true
    } else {
        textInputLayout.error = error
        false
    }
}

fun Context.showCustomizeToast(message: String?,@RawRes animUrl: Int,@DrawableRes  background:Int,@ColorInt color:Int?) {
    val binding = LayoutToastMessageBinding.inflate(LayoutInflater.from(this))
    binding.lottieAnimation.setAnimation(animUrl)
    binding.tvToastMessage.setTextColor(color ?: ContextCompat.getColor(this, R.color.white))
    binding.tvToastMessage.text = message?: ""
    binding.root.setBackgroundResource(background)
    Toast(this).apply {
        duration = Toast.LENGTH_SHORT
        setGravity(Gravity.TOP, 0, 150)
        view = binding.root
    }.show()

    if (job?.isActive == true) return

    job = CoroutineScope(Dispatchers.Main).launch {
        delay(500)
        binding.lottieAnimation.visible()
        binding.lottieAnimation.playAnimation()
        delay(2000)
        binding.lottieAnimation.invisible()
    }


}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    backgroundColor: Int? = null
) {
    val snackbar = Snackbar.make(this, message, duration)
    backgroundColor?.let {
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context, it))
    }
    snackbar.show()
}


fun Context.showAlertDialog(
    title: String? = null,
    message: String,
    positiveButtonText: String = "OK",
    positiveButtonAction: (() -> Unit)? = null,
    negativeButtonText: String? = null,
    negativeButtonAction: (() -> Unit)? = null,
    cancelable: Boolean = true
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(cancelable)
        .setPositiveButton(positiveButtonText) { dialog, _ ->
            positiveButtonAction?.invoke()
            dialog.dismiss()
        }
        .apply {
            if (negativeButtonText != null) {
                setNegativeButton(negativeButtonText) { dialog, _ ->
                    negativeButtonAction?.invoke()
                    dialog.dismiss()
                }
            }
        }
        .show()
}


fun ImageView.setImage(url: String) {
    if (url.isNotEmpty()) {
        Picasso.get().load(url).into(this)
    }

}

infix fun Int.calculateDiscount(discount: Int) = this.toFloat() - (this.toFloat() * discount / 100)


val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

