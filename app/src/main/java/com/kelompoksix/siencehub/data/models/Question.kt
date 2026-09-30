package com.kelompoksix.siencehub.data.models

data class Question(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String = ""
)