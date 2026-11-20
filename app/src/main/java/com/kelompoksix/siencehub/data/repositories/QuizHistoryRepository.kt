package com.kelompoksix.siencehub.data.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.kelompoksix.siencehub.data.models.QuizHistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object QuizHistoryRepository {
    private const val PREF_NAME = "sciencehub_quiz_history_pref"
    private const val KEY_HISTORY_JSON = "key_history_json"

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val gson = Gson()

    private val localHistoryList = mutableListOf<QuizHistoryItem>()

    private val _quizHistoryFlow = MutableStateFlow<List<QuizHistoryItem>>(emptyList())
    val quizHistoryFlow: StateFlow<List<QuizHistoryItem>> = _quizHistoryFlow.asStateFlow()

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Memuat riwayat kuis dari SharedPreferences lokal
    fun loadLocalHistory(context: Context) {
        val json = getPreferences(context).getString(KEY_HISTORY_JSON, null)
        if (!json.isNullOrBlank()) {
            try {
                val type = object : TypeToken<List<QuizHistoryItem>>() {}.type
                val savedList: List<QuizHistoryItem> = gson.fromJson(json, type) ?: emptyList()
                localHistoryList.clear()
                localHistoryList.addAll(savedList)
                _quizHistoryFlow.value = localHistoryList.toList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Menyimpan riwayat kuis ke SharedPreferences + Firestore
    fun simpanHasilKuis(
        context: Context? = null,
        subjectName: String,
        score: Int,
        totalQuestions: Int,
        correctCount: Int,
        wrongCount: Int,
        onSuccess: () -> Unit = {}
    ) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val idUnik = UUID.randomUUID().toString()

        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: "user_local"
        val userName = currentUser?.displayName ?: "Siswa"
        val userEmail = currentUser?.email ?: ""

        val newItem = QuizHistoryItem(
            id = idUnik,
            userId = userId,
            userName = userName,
            subjectName = subjectName,
            score = score,
            totalQuestions = totalQuestions,
            correctCount = correctCount,
            wrongCount = wrongCount,
            timestamp = System.currentTimeMillis(),
            formattedDate = currentDate
        )

        // 1. Tambahkan ke list & simpan ke SharedPreferences lokal (instan)
        localHistoryList.removeAll { it.id == idUnik }
        localHistoryList.add(0, newItem)
        _quizHistoryFlow.value = localHistoryList.toList()

        if (context != null) {
            try {
                val json = gson.toJson(localHistoryList)
                getPreferences(context).edit { putString(KEY_HISTORY_JSON, json) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 2. Simpan ke Cloud Firestore di background
        val historyItemMap = hashMapOf<String, Any>(
            "id" to idUnik,
            "userId" to userId,
            "userName" to userName,
            "userEmail" to userEmail,
            "subjectName" to subjectName,
            "score" to score,
            "totalQuestions" to totalQuestions,
            "correctCount" to correctCount,
            "wrongCount" to wrongCount,
            "timestamp" to System.currentTimeMillis(),
            "formattedDate" to currentDate
        )

        try {
            val docRef = db.collection("quiz_results").document(idUnik)
            docRef.set(historyItemMap).addOnSuccessListener {
                if (currentUser != null) {
                    db.collection("users")
                        .document(currentUser.uid)
                        .collection("quiz_history")
                        .document(idUnik)
                        .set(historyItemMap)
                }
                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onSuccess()
        }
    }

    // Mendengarkan Riwayat Kuis User Aktif dari Cloud Firestore
    fun fetchUserQuizHistory(context: Context? = null) {
        if (context != null && localHistoryList.isEmpty()) {
            loadLocalHistory(context)
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .collection("quiz_history")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null && !snapshot.isEmpty) {
                        val cloudHistoryList = snapshot.documents.mapNotNull { doc ->
                            QuizHistoryItem(
                                id = doc.getString("id") ?: doc.id,
                                userId = doc.getString("userId") ?: "",
                                userName = doc.getString("userName") ?: "Siswa",
                                subjectName = doc.getString("subjectName") ?: "Sains",
                                score = (doc.getLong("score") ?: 0L).toInt(),
                                totalQuestions = (doc.getLong("totalQuestions") ?: 0L).toInt(),
                                correctCount = (doc.getLong("correctCount") ?: 0L).toInt(),
                                wrongCount = (doc.getLong("wrongCount") ?: 0L).toInt(),
                                timestamp = doc.getLong("timestamp") ?: 0L,
                                formattedDate = doc.getString("formattedDate") ?: ""
                            )
                        }

                        val combined = (cloudHistoryList + localHistoryList)
                            .distinctBy { it.id }
                            .sortedByDescending { it.timestamp }

                        localHistoryList.clear()
                        localHistoryList.addAll(combined)
                        _quizHistoryFlow.value = combined

                        if (context != null) {
                            try {
                                val json = gson.toJson(combined)
                                getPreferences(context).edit { putString(KEY_HISTORY_JSON, json) }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
        }
    }
}
