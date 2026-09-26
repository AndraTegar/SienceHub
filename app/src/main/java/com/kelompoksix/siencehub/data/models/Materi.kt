package com.kelompoksix.siencehub.data.models

data class Materi(
    val id: Int,
    val judul: String,
    val deskripsi: String,
    val kategori: String,
    val durasi: String,
    val tingkatKesulitan: String = "Pemula",
    val isiKonten: String = ""
)
