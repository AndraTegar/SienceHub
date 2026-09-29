package com.kelompoksix.siencehub.data.repositories

// ==========================================
// 1. MODEL DATA KOMPONEN (Bentuk Blok Materi)
// ==========================================
sealed class KomponenMateri {
    // Blok Kartu Pertanyaan (seperti gambar bus)
    data class PertanyaanPemantik(val teks: String, val idGambarDrawable: Int? = null) : KomponenMateri()

    // Blok Kartu Cek Pemahaman (Garis merah)
    data class CekPemahaman(val jumlahGaris: Int = 2) : KomponenMateri()

    // Blok Kartu Rumus (seperti gambar segitiga)
    data class Rumus(val teksKiri: String, val idGambarDrawable: Int? = null) : KomponenMateri()

    // Blok Kartu Ilustrasi Besar (seperti gambar meja)
    data class Ilustrasi(val keterangan: String, val idGambarDrawable: Int? = null) : KomponenMateri()

    // Blok Teks Paragraf Biasa
    data class TeksBiasa(val teks: String) : KomponenMateri()
}

// Model untuk satu Bab utuh
data class KontenBab(
    val idBab: Int,
    val judulBab: String,
    val daftarKomponen: List<KomponenMateri> // Kumpulan blok-blok materi di atas
)

// ==========================================
// 2. DATABASE DUMMY (Tempat Anda Mengubah Isi Materi Nanti)
// ==========================================
object IsiMateriRepository {
    fun getIsiMateriFisika(): List<KontenBab> {
        return listOf(
            KontenBab(
                idBab = 1,
                judulBab = "Hukum Newton I",
                daftarKomponen = listOf(
                    // Anda tinggal menyusun blok-bloknya di sini! Sangat mudah diubah.
                    KomponenMateri.PertanyaanPemantik(
                        teks = "Pernahkah badanmu terdorong ke depan saat bus direm mendadak?"
                        // idGambarDrawable = R.drawable.bus_ilustrasi (Hapus komentar jika gambar sudah ada)
                    ),
                    KomponenMateri.CekPemahaman(),
                    KomponenMateri.Rumus(
                        teksKiri = "Rumus\nHukum\nNewton 1"
                        // idGambarDrawable = R.drawable.rumus_segitiga
                    ),
                    KomponenMateri.Ilustrasi(
                        keterangan = "Ilustrasi Balok & Meja"
                        // idGambarDrawable = R.drawable.ilustrasi_meja
                    )
                )
            ),
            KontenBab(
                idBab = 2,
                judulBab = "Hukum Newton II",
                daftarKomponen = listOf(
                    KomponenMateri.TeksBiasa("Hukum II Newton menjelaskan hubungan antara gaya, massa, dan percepatan."),
                    KomponenMateri.PertanyaanPemantik("Mengapa mendorong truk terasa lebih berat daripada mendorong sepeda?"),
                    KomponenMateri.Rumus("Rumus\nHukum\nNewton 2\n(F = m x a)")
                )
            ),
            KontenBab(
                idBab = 3,
                judulBab = "Hukum Newton III",
                daftarKomponen = listOf(
                    KomponenMateri.TeksBiasa("Untuk setiap aksi, selalu ada reaksi yang sama besar dan berlawanan arah."),
                    KomponenMateri.Ilustrasi("Ilustrasi Roket Meluncur")
                )
            )
        )
    }

    fun getBabById(idBab: Int): KontenBab? {
        return getIsiMateriFisika().find { it.idBab == idBab }
    }
}