package com.example.buybuy.util

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.buybuy.util.Constant.PREFS_NAME
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

fun View.Visible() {
    visibility = View.VISIBLE
}

fun View.Gone() {
    visibility = View.GONE
}
fun View.Invisible() {
    visibility = View.INVISIBLE
}

fun TextInputEditText.Checkemail(error: String):Boolean{
    val textInputLayout = this.parent.parent as TextInputLayout
    return if(android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()){
        textInputLayout.isErrorEnabled = false
        true
    }
    else{
        textInputLayout.error = error
        false
    }

}
fun TextInputEditText.CheckNullorEmpty(error:String):Boolean{
    val textInputLayout = this.parent.parent as TextInputLayout
    return if (text.toString().isNotEmpty()&&text.toString().isNotBlank()){
        textInputLayout.isErrorEnabled=false
         true
    }
    else{
        textInputLayout.error=error
        false
    }
}

fun Context.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}


fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT, backgroundColor: Int? = null) {
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


fun ImageView.setImage(url:String){
    Picasso.get().load(url).into(this)

}
infix fun Int.calculateDiscount(discount:Int)=this.toFloat()-(this.toFloat()*discount/100)


val Context.sharedPreferences: SharedPreferences
get() = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

