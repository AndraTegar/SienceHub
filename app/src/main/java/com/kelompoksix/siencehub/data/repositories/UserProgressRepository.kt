package com.kelompoksix.siencehub.data.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserProgressRepository {
    private const val PREF_NAME = "sciencehub_user_progress"
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Key map progress per kategori (misal "Fisika" -> 1.0f)
    private val _subjectProgressFlow = MutableStateFlow<Map<String, Float>>(
        mapOf(
            "Biologi" to 0.2f,
            "Fisika" to 0.0f,
            "Kimia" to 0.4f
        )
    )
    val subjectProgressFlow: StateFlow<Map<String, Float>> = _subjectProgressFlow.asStateFlow()

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Memuat progress tersimpan
    fun loadProgress(context: Context) {
        val prefs = getPreferences(context)
        val fisikaProgress = prefs.getFloat("progress_Fisika", 0.0f)
        val biologiProgress = prefs.getFloat("progress_Biologi", 0.2f)
        val kimiaProgress = prefs.getFloat("progress_Kimia", 0.4f)

        _subjectProgressFlow.value = mapOf(
            "Biologi" to biologiProgress,
            "Fisika" to fisikaProgress,
            "Kimia" to kimiaProgress
        )

        // Sinkronkan juga dari Cloud Firestore jika login
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .collection("progress")
                .addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null && !snapshot.isEmpty) {
                        val currentMap = _subjectProgressFlow.value.toMutableMap()
                        snapshot.documents.forEach { doc ->
                            val subject = doc.id
                            val progress = (doc.getDouble("progress") ?: 0.0).toFloat()
                            currentMap[subject] = progress
                            prefs.edit { putFloat("progress_$subject", progress) }
                        }
                        _subjectProgressFlow.value = currentMap
                    }
                }
        }
    }

    // Memperbarui progress suatu mata pelajaran (misal 1.0f = 100%)
    fun updateSubjectProgress(context: Context, subjectName: String, progress: Float) {
        val prefs = getPreferences(context)
        val normalizedSubject = when {
            subjectName.contains("Fisika", ignoreCase = true) -> "Fisika"
            subjectName.contains("Biologi", ignoreCase = true) -> "Biologi"
            subjectName.contains("Kimia", ignoreCase = true) -> "Kimia"
            else -> subjectName
        }

        val currentMap = _subjectProgressFlow.value.toMutableMap()
        val oldProgress = currentMap[normalizedSubject] ?: 0f
        val newProgress = maxOf(oldProgress, progress.coerceIn(0f, 1f))

        currentMap[normalizedSubject] = newProgress
        _subjectProgressFlow.value = currentMap

        prefs.edit { putFloat("progress_$normalizedSubject", newProgress) }

        // Simpan ke Cloud Firestore
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val progressData = hashMapOf(
                "subject" to normalizedSubject,
                "progress" to newProgress.toDouble(),
                "lastUpdated" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(currentUser.uid)
                .collection("progress")
                .document(normalizedSubject)
                .set(progressData, com.google.firebase.firestore.SetOptions.merge())
        }
    }

    fun getProgressForSubject(subjectName: String): Float {
        val normalizedSubject = when {
            subjectName.contains("Fisika", ignoreCase = true) -> "Fisika"
            subjectName.contains("Biologi", ignoreCase = true) -> "Biologi"
            subjectName.contains("Kimia", ignoreCase = true) -> "Kimia"
            else -> subjectName
        }
        return _subjectProgressFlow.value[normalizedSubject] ?: 0f
    }
}
