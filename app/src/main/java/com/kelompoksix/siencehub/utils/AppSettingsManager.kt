package com.kelompoksix.siencehub.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit

object AppSettingsManager {
    private const val PREF_NAME = "sciencehub_app_settings"
    private const val KEY_DARK_MODE = "dark_mode_enabled"
    private const val KEY_NOTIFICATIONS = "push_notifications_enabled"

    var isDarkMode by mutableStateOf(false)
        private set

    var isNotificationsEnabled by mutableStateOf(true)
        private set

    private var initialized = false

    private fun getPrefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun init(context: Context) {
        if (initialized) return
        val prefs = getPrefs(context)
        isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false)
        isNotificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        initialized = true
    }

    fun setDarkMode(context: Context, enabled: Boolean) {
        isDarkMode = enabled
        getPrefs(context).edit { putBoolean(KEY_DARK_MODE, enabled) }
    }

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        isNotificationsEnabled = enabled
        getPrefs(context).edit { putBoolean(KEY_NOTIFICATIONS, enabled) }
    }
}
