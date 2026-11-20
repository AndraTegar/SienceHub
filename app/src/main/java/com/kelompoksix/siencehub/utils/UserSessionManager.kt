package com.kelompoksix.siencehub.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

object UserSessionManager {
    private const val PREF_NAME = "sciencehub_user_session"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_PHONE = "user_phone"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(context: Context, email: String, name: String? = null, phone: String? = null) {
        val displayName = name?.takeIf { it.isNotBlank() } ?: extractNameFromEmail(email)
        getPreferences(context).edit {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, displayName)
            if (phone != null) putString(KEY_USER_PHONE, phone)
        }
    }

    fun updateProfile(context: Context, newName: String, newPhone: String) {
        getPreferences(context).edit {
            putString(KEY_USER_NAME, newName)
            putString(KEY_USER_PHONE, newPhone)
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) return true
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(context: Context): String {
        val firebaseEmail = FirebaseAuth.getInstance().currentUser?.email
        if (!firebaseEmail.isNullOrBlank()) return firebaseEmail
        return getPreferences(context).getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun getUserName(context: Context): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val firebaseDisplayName = firebaseUser?.displayName
        if (!firebaseDisplayName.isNullOrBlank()) return firebaseDisplayName
        val savedName = getPreferences(context).getString(KEY_USER_NAME, "") ?: ""
        if (savedName.isNotBlank()) return savedName
        val email = getUserEmail(context)
        return extractNameFromEmail(email)
    }

    fun getUserPhone(context: Context): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val firebasePhone = firebaseUser?.phoneNumber
        if (!firebasePhone.isNullOrBlank()) return firebasePhone
        return getPreferences(context).getString(KEY_USER_PHONE, "") ?: ""
    }

    fun clearSession(context: Context) {
        try {
            FirebaseAuth.getInstance().signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
