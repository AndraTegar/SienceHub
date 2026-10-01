package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.BabMateri
import com.kelompoksix.siencehub.data.models.TopikMateri

object MateriRepository {
    fun getDaftarTopik(): List<TopikMateri> {
        return listOf(
            // ==========================================
            // MATA PELAJARAN LAMA
            // ==========================================
            TopikMateri(
                id = "biologi_sel",
                judul = "Biologi: Struktur Sel",
                kategori = "Biologi",
                daftarBab = listOf(
                    BabMateri(1, "Pengenalan Sel & Sejarahnya", "10 menit", true),
                    BabMateri(2, "Organel Sel dan Fungsinya", "15 menit", true),
                    BabMateri(3, "Perbedaan Sel Hewan dan Tumbuhan", "12 menit", false),
                    BabMateri(4, "Transpor Membran Sel", "20 menit", false)
                )
            ),
            TopikMateri(
                id = "fisika_newton",
                judul = "Fisika: Hukum Newton",
                kategori = "Fisika",
                daftarBab = listOf(
                    BabMateri(1, "Konsep Dasar Gaya dan Inersia (Hukum I)", "12 menit", true),
                    BabMateri(2, "Hubungan Gaya, Massa, & Percepatan (Hukum II)", "18 menit", false),
                    BabMateri(3, "Hukum Aksi Reaksi (Hukum III)", "15 menit", false),
                    BabMateri(4, "Penerapan Hukum Newton dalam Kehidupan", "15 menit", false)
                )
            ),
            TopikMateri(
                id = "kimia_asam_basa",
                judul = "Kimia: Reaksi Asam Basa",
                kategori = "Kimia",
                daftarBab = listOf(
                    BabMateri(1, "Sifat Larutan Asam dan Basa", "10 menit", true),
                    BabMateri(2, "Teori Asam Basa (Arrhenius & Bronsted-Lowry)", "15 menit", true),
                    BabMateri(3, "Indikator Alami dan Buatan", "12 menit", true),
                    BabMateri(4, "Perhitungan Skala pH dan pOH", "25 menit", false)
                )
            ),
            TopikMateri(
                id = "astronomi_tata_surya",
                judul = "Astronomi: Tata Surya",
                kategori = "Astronomi",
                daftarBab = listOf(
                    BabMateri(1, "Pengenalan Tata Surya & Matahari", "12 menit", true),
                    BabMateri(2, "Planet Dalam dan Planet Luar", "18 menit", false),
                    BabMateri(3, "Bulan, Asteroid, dan Komet", "15 menit", false),
                    BabMateri(4, "Gerak Planet (Hukum Kepler)", "20 menit", false)
                )
            ),
            TopikMateri(
                id = "matematika_aljabar",
                judul = "Matematika: Persamaan Linear",
                kategori = "Math",
                daftarBab = listOf(
                    BabMateri(1, "Konsep Variabel dan Persamaan Linear", "10 menit", true),
                    BabMateri(2, "Persamaan Linear Satu Variabel", "15 menit", false),
                    BabMateri(3, "Sistem Persamaan Linear Dua Variabel", "20 menit", false),
                    BabMateri(4, "Penerapan dalam Soal Cerita", "18 menit", false)
                )
            ),
            TopikMateri(
                id = "geografi_litosfer",
                judul = "Geografi: Dinamika Litosfer",
                kategori = "Geografi",
                daftarBab = listOf(
                    BabMateri(1, "Struktur Lapisan Bumi", "12 menit", true),
                    BabMateri(2, "Tektonik Lempeng dan Pergerakannya", "18 menit", false),
                    BabMateri(3, "Vulkanisme dan Gempa Bumi", "15 menit", false),
                    BabMateri(4, "Mitigasi Bencana Alam di Indonesia", "15 menit", false)
                )
            )
        )
    }
}