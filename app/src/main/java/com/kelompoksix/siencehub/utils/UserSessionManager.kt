package com.kelompoksix.siencehub.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object UserSessionManager {
    private const val PREF_NAME = "sciencehub_user_session"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_NAME = "user_name"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(context: Context, email: String, name: String? = null) {
        val displayName = name?.takeIf { it.isNotBlank() } ?: extractNameFromEmail(email)
        getPreferences(context).edit {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, displayName)
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(context: Context): String {
        return getPreferences(context).getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun getUserName(context: Context): String {
        return getPreferences(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun clearSession(context: Context) {
        getPreferences(context).edit {
            clear()
        }
    }

    private fun extractNameFromEmail(email: String): String {
        if (email.isBlank() || !email.contains("@")) return "Pengguna ScienceHub"
        val prefix = email.substringBefore("@")
        return prefix.replace(".", " ")
            .split(" ")
            .joinToString(" ") { part ->
                part.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }
}
