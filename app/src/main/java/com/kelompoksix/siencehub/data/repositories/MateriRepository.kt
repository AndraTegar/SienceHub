package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.Materi

object MateriRepository {
    fun getDaftarMateri(): List<Materi> {
        return listOf(
            Materi(
                id = "1",
                judul = "Biologi: Struktur Sel",
                kategori = "Biologi",
                deskripsi = "Mempelajari bagian-bagian penting sel hewan dan tumbuhan beserta fungsinya."
            ),
            Materi(
                id = "2",
                judul = "Fisika: Hukum Newton",
                kategori = "Fisika",
                deskripsi = "Memahami konsep gaya, massa, dan percepatan dalam hukum gerak Newton."
            ),
            Materi(
                id = "3",
                judul = "Kimia: Reaksi Asam Basa",
                kategori = "Kimia",
                deskripsi = "Mengenal sifat larutan asam dan basa serta indikator pengujinya."
            )
        )
    }
}