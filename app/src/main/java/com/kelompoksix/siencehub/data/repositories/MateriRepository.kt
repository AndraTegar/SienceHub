package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.Materi

object MateriRepository {
    fun getDaftarMateri(): List<Materi> {
        return listOf(
            Materi(
                id = 1,
                judul = "Hukum Newton & Gerak",
                deskripsi = "Pelajari dasar-dasar mekanika klasik, Hukum I, II, dan III Newton tentang gerak serta gaya.",
                kategori = "Fisika",
                durasi = "15 Menit",
                tingkatKesulitan = "Pemula",
                isiKonten = "Hukum I Newton (Inersia), Hukum II Newton (F = m.a), Hukum III Newton (Aksi-Reaksi)."
            ),
            Materi(
                id = 2,
                judul = "Fotosintesis & Biologi Tumbuhan",
                deskripsi = "Memahami reaksi terang dan gelap fotosintesis pada tumbuhan serta peran klorofil.",
                kategori = "Biologi",
                durasi = "20 Menit",
                tingkatKesulitan = "Menengah",
                isiKonten = "Fotosintesis mengubah energi cahaya menjadi energi kimia berupa glukosa."
            ),
            Materi(
                id = 3,
                judul = "Struktur Atom & Tabel Periodik",
                deskripsi = "Mengenal komponen atom (proton, neutron, elektron) dan susunan unsur dalam tabel periodik.",
                kategori = "Kimia",
                durasi = "25 Menit",
                tingkatKesulitan = "Pemula",
                isiKonten = "Atom terdiri dari inti atom dan awan elektron."
            ),
            Materi(
                id = 4,
                judul = "Sistem Tata Surya & Planet",
                deskripsi = "Eksplorasi planet-planet, matahari, asteroid, dan fenomena astronomi di tata surya.",
                kategori = "Astronomi",
                durasi = "18 Menit",
                tingkatKesulitan = "Pemula",
                isiKonten = "Tata surya terdiri dari Matahari dan 8 planet utama."
            ),
            Materi(
                id = 5,
                judul = "Termodinamika & Panas",
                deskripsi = "Konsep perpindahan energi panas, asas Black, dan Hukum Termodinamika.",
                kategori = "Fisika",
                durasi = "30 Menit",
                tingkatKesulitan = "Lanjut",
                isiKonten = "Termodinamika mempelajari pergerakan panas dan konversi energi."
            )
        )
    }

    fun getMateriById(id: Int): Materi? {
        return getDaftarMateri().find { it.id == id }
    }
}
