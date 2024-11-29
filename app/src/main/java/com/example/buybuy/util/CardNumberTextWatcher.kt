package com.example.buybuy.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CardNumberTextWatcher (private val editText: EditText,private val onTextChanged: (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if (s == null) return

        val trimmed = s.toString().replace(" ", "")
        val formatted = StringBuilder()

        for (i in trimmed.indices) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ")
            }
            formatted.append(trimmed[i])
        }
        editText.removeTextChangedListener(this)
        editText.setText(formatted.toString())
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)

    }

    override fun afterTextChanged(s: Editable?) {
        onTextChanged(s.toString())

    }
}