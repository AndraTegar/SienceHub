package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.Notifikasi
import com.kelompoksix.siencehub.data.models.TipeNotifikasi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotifikasiRepository {

    private val _notifikasiList = MutableStateFlow<List<Notifikasi>>(
        listOf(
            Notifikasi(
                id = "1",
                judul = "Streak 6 Hari! 🔥",
                pesan = "Luar biasa! Kamu sudah belajar 6 hari berturut-turut. Pertahankan semangatmu!",
                waktu = "10 menit lalu",
                isDibaca = false,
                tipe = TipeNotifikasi.STREAK
            ),
            Notifikasi(
                id = "2",
                judul = "Materi Baru Tersedia 📚",
                pesan = "Materi 'Hukum Gravitasi Newton' di modul Fisika telah diperbarui dengan simulasi interaktif.",
                waktu = "1 jam lalu",
                isDibaca = false,
                tipe = TipeNotifikasi.MATERI
            ),
            Notifikasi(
                id = "3",
                judul = "Tantangan Kuis Kimia 🧪",
                pesan = "Kuis Reaksi Asam Basa siap diuji! Selesaikan kuis dan kumpulkan skor terbaikmu.",
                waktu = "3 jam lalu",
                isDibaca = false,
                tipe = TipeNotifikasi.KUIS
            ),
            Notifikasi(
                id = "4",
                judul = "Eksperimen Virtual Newton 🔬",
                pesan = "Cobalah simulator gaya dan percepatan di laboratorium virtual SienceHub.",
                waktu = "Kemarin",
                isDibaca = true,
                tipe = TipeNotifikasi.EKSPERIMEN
            ),
            Notifikasi(
                id = "5",
                judul = "Fakta Sains Hari Ini 💡",
                pesan = "Tahukah kamu? Cahaya matahari membutuhkan waktu sekitar 8 menit 20 detik untuk mencapai bumi.",
                waktu = "2 hari lalu",
                isDibaca = true,
                tipe = TipeNotifikasi.INFO
            )
        )
    )

    val notifikasiList: StateFlow<List<Notifikasi>> = _notifikasiList.asStateFlow()

    fun tandaiDibaca(id: String) {
        _notifikasiList.value = _notifikasiList.value.map { notif ->
            if (notif.id == id) notif.copy(isDibaca = true) else notif
        }
    }

    fun tandaiSemuaDibaca() {
        _notifikasiList.value = _notifikasiList.value.map { notif ->
            notif.copy(isDibaca = true)
        }
    }

    fun hapusNotifikasi(id: String) {
        _notifikasiList.value = _notifikasiList.value.filter { it.id != id }
    }

    fun getJumlahBelumDibaca(): Int {
        return _notifikasiList.value.count { !it.isDibaca }
    }
}
