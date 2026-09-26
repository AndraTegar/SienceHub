package com.kelompoksix.siencehub.data.models

data class SoalKuis(
    val id: Int,
    val pertanyaan: String,
    val opsi: List<String>,
    val jawabanBenar: Int
)
