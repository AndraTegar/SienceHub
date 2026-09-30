package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.Soal

object QuizRepository {
    fun getSoalBySubject(subjectName: String): List<Soal> {
        return when (subjectName.lowercase()) {
            "fisika" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Sebuah benda akan tetap diam atau bergerak lurus beraturan jika tidak ada gaya luar yang bekerja padanya. Hal ini sesuai dengan...",
                    opsi = listOf("Hukum I Newton", "Hukum II Newton", "Hukum III Newton", "Hukum Kepler"),
                    jawabanBenar = 0,
                    pembahasan = "Hukum I Newton menjelaskan tentang kelembaman/inersia benda."
                ),
                Soal(
                    id = 2,
                    pertanyaan = "Besarnya percepatan yang dialami benda berbanding lurus dengan gaya dan berbanding terbalik dengan...",
                    opsi = listOf("Kecepatan", "Massa benda", "Jarak", "Waktu"),
                    jawabanBenar = 1,
                    pembahasan = "Berdasarkan Hukum II Newton (F = m * a), percepatan berbanding terbalik dengan massa (a = F / m)."
                )
            )
            "astronomi" -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Planet terbesar di Tata Surya adalah...",
                    opsi = listOf("Mars", "Saturnus", "Yupiter", "Neptunus"),
                    jawabanBenar = 2,
                    pembahasan = "Yupiter adalah planet terbesar di Sistem Tata Surya kita."
                )
            )
            else -> listOf(
                Soal(
                    id = 1,
                    pertanyaan = "Contoh soal default untuk mata pelajaran ini...",
                    opsi = listOf("Pilihan A", "Pilihan B", "Pilihan C", "Pilihan D"),
                    jawabanBenar = 0,
                    pembahasan = "Pembahasan soal default."
                )
            )
        }
    }
}