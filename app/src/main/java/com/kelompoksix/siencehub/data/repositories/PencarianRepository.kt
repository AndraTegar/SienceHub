package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.BabMateri
import com.kelompoksix.siencehub.data.models.TopikMateri

data class HasilCari(val topik: TopikMateri, val bab: BabMateri?)

object PencarianRepository {
    fun cari(query: String): List<HasilCari> {
        val q = query.trim()
        if (q.isEmpty()) return emptyList()

        val hasil = mutableListOf<HasilCari>()
        MateriRepository.getDaftarTopik().forEach { topik ->
            // cocok dengan nama mapel / judul topik
            if (topik.judul.contains(q, ignoreCase = true) ||
                topik.kategori.contains(q, ignoreCase = true)
            ) hasil += HasilCari(topik, null)

            // cocok dengan judul bab
            topik.daftarBab
                .filter { it.judulBab.contains(q, ignoreCase = true) }
                .forEach { hasil += HasilCari(topik, it) }
        }
        return hasil
    }
}