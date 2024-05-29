package com.example.cineconnect.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.cineconnect.R

class SharedPrefs(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun setStringValue(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getStringValue(key: String): String? {
        return prefs.getString(key, null)
    }

    fun setIntValue(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getIntValue(key: String, i: Int): Int {
        return prefs.getInt(key, i)
    }

    fun clearValue() {
        prefs.edit().clear().apply()
    }
}
