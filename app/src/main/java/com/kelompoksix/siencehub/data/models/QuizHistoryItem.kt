package com.kelompoksix.siencehub.data.models

data class QuizHistoryItem(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val subjectName: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val formattedDate: String = ""
)
