package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.Notifikasi
import com.kelompoksix.siencehub.data.models.TipeNotifikasi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object NotifikasiRepository {

    private val _notifikasiList = MutableStateFlow<List<Notifikasi>>(
        listOf(
            Notifikasi(
                id = "welcome_materi",
                judul = "Materi & Evaluasi Lengkap 📚",
                pesan = "Seluruh 6 mata pelajaran kini dilengkapi 5 bab bertahap dan kuis evaluasi interaktif.",
                waktu = "Hari ini",
                isDibaca = false,
                tipe = TipeNotifikasi.MATERI
            ),
            Notifikasi(
                id = "welcome_eksperimen",
                judul = "Eksperimen Virtual Sains 🔬",
                pesan = "Uji langsung Hukum II Newton, gravitasi planet, indikator pH, dan timbangan aljabar.",
                waktu = "Kemarin",
                isDibaca = true,
                tipe = TipeNotifikasi.EKSPERIMEN
            ),
            Notifikasi(
                id = "welcome_fakta",
                judul = "Fakta Sains Hari Ini 💡",
                pesan = "Tahukah kamu? Cahaya matahari membutuhkan waktu sekitar 8 menit 20 detik untuk mencapai Bumi.",
                waktu = "2 hari lalu",
                isDibaca = true,
                tipe = TipeNotifikasi.INFO
            )
        )
    )

    val notifikasiList: StateFlow<List<Notifikasi>> = _notifikasiList.asStateFlow()

    fun tambahNotifikasi(
        judul: String,
        pesan: String,
        tipe: TipeNotifikasi = TipeNotifikasi.INFO,
        waktu: String = "Baru saja"
    ) {
        val existing = _notifikasiList.value
        if (existing.firstOrNull()?.let { it.judul == judul && it.pesan == pesan } == true) {
            return
        }
        val itemBaru = Notifikasi(
            id = UUID.randomUUID().toString(),
            judul = judul,
            pesan = pesan,
            waktu = waktu,
            isDibaca = false,
            tipe = tipe
        )
        _notifikasiList.value = listOf(itemBaru) + existing
    }

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
