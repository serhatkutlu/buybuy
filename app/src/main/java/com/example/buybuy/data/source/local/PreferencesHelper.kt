package com.example.buybuy.data.source.local

import android.content.Context
import java.time.LocalDateTime

class PreferencesHelper(private val context:Context) {

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    companion object {
        private const val PREF_NAME = "MyPrefs"
        private const val END_TIME_KEY = "endTime"

    }

    fun createEndTime() {
        val endTime = LocalDateTime.now().plusDays(1).toString()
        sharedPreferences.edit().putString(END_TIME_KEY, endTime).apply()
    }
    fun getEndTime(): String? {
        return sharedPreferences.getString(END_TIME_KEY, null)
    }


}