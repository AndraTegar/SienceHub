package com.kelompoksix.siencehub.data.models

data class BabMateri(
    val id: Int,
    val judulBab: String,
    val durasi: String,
    val sudahSelesai: Boolean
)

data class TopikMateri(
    val id: String,
    val judul: String,
    val kategori: String,
    val daftarBab: List<BabMateri>
)