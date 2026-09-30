package com.kelompoksix.siencehub.data.models

import androidx.annotation.DrawableRes

sealed class BlokKonten {
    // Kartu pembuka: gambar kiri + teks kanan
    data class TeksGambar(val teks: String, @DrawableRes val gambar: Int? = null) : BlokKonten()
    // Kartu rumus: teks kiri + gambar kanan
    data class Rumus(val judul: String, val isi: String, @DrawableRes val gambar: Int? = null) : BlokKonten()
    // Kartu gambar penuh
    data class Gambar(@DrawableRes val gambar: Int, val keterangan: String? = null) : BlokKonten()
    // Kartu teks biasa
    data class Paragraf(val judul: String?, val teks: String) : BlokKonten()
}

data class SoalMini(
    val pertanyaan: String,
    val opsi: List<String>,
    val jawabanBenar: Int, // index opsi, mulai 0
    val pembahasan: String
)

data class IsiBab(
    val blok: List<BlokKonten>,
    val kuis: List<SoalMini>
)