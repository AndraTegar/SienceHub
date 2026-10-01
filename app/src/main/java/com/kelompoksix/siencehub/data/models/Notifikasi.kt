package com.kelompoksix.siencehub.data.models

enum class TipeNotifikasi {
    STREAK,
    MATERI,
    KUIS,
    EKSPERIMEN,
    INFO
}

data class Notifikasi(
    val id: String,
    val judul: String,
    val pesan: String,
    val waktu: String,
    val isDibaca: Boolean = false,
    val tipe: TipeNotifikasi = TipeNotifikasi.INFO
)
