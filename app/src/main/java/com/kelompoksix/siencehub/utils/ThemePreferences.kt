package com.kelompoksix.siencehub.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit

class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_settings", Context.MODE_PRIVATE)

    var isDarkMode: Boolean by mutableStateOf(prefs.getBoolean("key_dark_mode", false))
        private set

    fun updateDarkMode(enabled: Boolean) {
        isDarkMode = enabled
        prefs.edit { putBoolean("key_dark_mode", enabled) }
    }
}
