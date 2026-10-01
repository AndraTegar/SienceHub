package com.kelompoksix.siencehub.data.models

data class Soal(
    val id: Int,
    val pertanyaan: String,
    val opsi: List<String>,
    val jawabanBenar: Int, // Indeks pilihan jawaban yang benar (0, 1, 2, atau 3)
    val pembahasan: String
)