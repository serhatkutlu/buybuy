package com.example.buybuy.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext private val context:Context) {

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
    fun clearEndTime() {
        sharedPreferences.edit().remove(END_TIME_KEY).apply()
    }



}