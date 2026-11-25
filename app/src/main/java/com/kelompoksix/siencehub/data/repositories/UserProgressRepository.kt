package com.kelompoksix.siencehub.data.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserProgressRepository {
    private const val PREF_NAME = "sciencehub_user_progress_v2"
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var progressListener: ListenerRegistration? = null
    private var listeningUid: String? = null

    private val defaultProgressMap: Map<String, Float> =
        MateriRepository.ALL_SUBJECTS.associateWith { 0f }

    private val _subjectProgressFlow = MutableStateFlow<Map<String, Float>>(defaultProgressMap)
    val subjectProgressFlow: StateFlow<Map<String, Float>> = _subjectProgressFlow.asStateFlow()

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun userKeyPrefix(): String {
        val uid = auth.currentUser?.uid?.takeIf { it.isNotBlank() }
        val email = auth.currentUser?.email?.takeIf { it.isNotBlank() }
        return (uid ?: email ?: "local").lowercase()
    }

    fun loadProgress(context: Context) {
        val prefs = getPreferences(context)
        val prefix = userKeyPrefix()

        val loadedMap = MateriRepository.ALL_SUBJECTS.associateWith { subject ->
            prefs.getFloat("${prefix}_progress_$subject", 0f).coerceIn(0f, 1f)
        }
        _subjectProgressFlow.value = loadedMap

        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (listeningUid != currentUser.uid) {
                progressListener?.remove()
                listeningUid = currentUser.uid
                progressListener = db.collection("users")
                    .document(currentUser.uid)
                    .collection("progress")
                    .addSnapshotListener { snapshot, error ->
                        if (error == null && snapshot != null && !snapshot.isEmpty) {
                            val currentMap = _subjectProgressFlow.value.toMutableMap()
                            snapshot.documents.forEach { doc ->
                                val normalizedSubject = MateriRepository.normalizeSubjectName(
                                    doc.getString("subject") ?: doc.id
                                )
                                val cloudProg = (doc.getDouble("progress") ?: 0.0).toFloat().coerceIn(0f, 1f)
                                val merged = maxOf(currentMap[normalizedSubject] ?: 0f, cloudProg)
                                currentMap[normalizedSubject] = merged
                                prefs.edit { putFloat("${prefix}_progress_$normalizedSubject", merged) }
                            }
                            _subjectProgressFlow.value = currentMap
                        }
                    }
            }
        } else {
            progressListener?.remove()
            progressListener = null
            listeningUid = null
        }
    }

    fun updateSubjectProgress(context: Context, subjectName: String, progress: Float) {
        val prefs = getPreferences(context)
        val prefix = userKeyPrefix()
        val normalizedSubject = MateriRepository.normalizeSubjectName(subjectName)
        val clamped = progress.coerceIn(0f, 1f)

        val currentMap = _subjectProgressFlow.value.toMutableMap()
        val oldProgress = currentMap[normalizedSubject] ?: 0f
        val newProgress = maxOf(oldProgress, clamped)

        currentMap[normalizedSubject] = newProgress
        _subjectProgressFlow.value = currentMap

        prefs.edit { putFloat("${prefix}_progress_$normalizedSubject", newProgress) }

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
                .set(progressData, SetOptions.merge())
        }
    }

    fun getProgressForSubject(subjectName: String): Float {
        val normalizedSubject = MateriRepository.normalizeSubjectName(subjectName)
        return _subjectProgressFlow.value[normalizedSubject] ?: 0f
    }

    fun resetLocalProgress(context: Context) {
        val prefs = getPreferences(context)
        val prefix = userKeyPrefix()
        prefs.edit {
            MateriRepository.ALL_SUBJECTS.forEach { subject ->
                remove("${prefix}_progress_$subject")
            }
        }
        _subjectProgressFlow.value = defaultProgressMap
    }
}
