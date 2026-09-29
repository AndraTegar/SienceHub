package com.kelompoksix.siencehub.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.data.repositories.IsiMateriRepository
import com.kelompoksix.siencehub.data.repositories.KontenBab
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.ui.components.FloatingBottomNav

@Composable
fun KerangkaAplikasi(
    onLogout: () -> Unit
) {
    var indexAktif by remember { mutableIntStateOf(0) }

    // Status untuk halaman tambahan yang menutupi Navbar (Overlay Screen)
    var tampilkanPengaturan by remember { mutableStateOf(false) }
    var tampilkanAchievement by remember { mutableStateOf(false) }
    var tampilkanLeaderboard by remember { mutableStateOf(false) }

    // Status untuk menyimpan topik materi yang sedang dibuka (Daftar Bab)
    var topikAktif by remember { mutableStateOf<TopikMateri?>(null) }

    // TAMBAHAN: Status untuk menyimpan isi materi bab yang sedang dibaca
    var bacaBabAktif by remember { mutableStateOf<KontenBab?>(null) }

    // Tombol Back Android Handler (Menutup overlay secara berurutan / LIFO)
    BackHandler(enabled = indexAktif != 0 || tampilkanPengaturan || tampilkanAchievement || tampilkanLeaderboard || topikAktif != null || bacaBabAktif != null) {
        when {
            bacaBabAktif != null -> bacaBabAktif = null // 1. Tutup layar baca isi materi dulu
            topikAktif != null -> topikAktif = null     // 2. Kalau layar baca sudah tutup, baru tutup daftar bab
            tampilkanPengaturan -> tampilkanPengaturan = false
            tampilkanAchievement -> tampilkanAchievement = false
            tampilkanLeaderboard -> tampilkanLeaderboard = false
            else -> indexAktif = 0
        }
    }

    // ==========================================
    // ALUR ROUTING / PERPINDAHAN HALAMAN
    // ==========================================

    // Menampilkan layar penuh (overlay / detail) jika salah satu menu dipilih
    if (bacaBabAktif != null) {
        // TAMPILAN 1: Halaman Baca Isi Materi (Paling Depan)
        BacaMateriScreen(
            konten = bacaBabAktif!!,
            onBackClick = { bacaBabAktif = null },
            onMulaiKuisClick = { idBab ->
                // Logika ketika tombol kuis ditekan (Nanti akan diarahkan ke KuisScreen)
                println("Tombol Kuis Bab $idBab ditekan!")
            }
        )
    } else if (topikAktif != null) {
        // TAMPILAN 2: Halaman Daftar Bab
        DetailMateriScreen(
            topikMateri = topikAktif!!,
            onBackClick = { topikAktif = null },
            onBabClick = { idBab ->
                // TAMBAHAN: Mengambil isi bab dari repository saat kartu bab diklik
                val isiBab = IsiMateriRepository.getBabById(idBab)
                if (isiBab != null) {
                    bacaBabAktif = isiBab
                }
            }
        )
    } else if (tampilkanPengaturan) {
        SettingScreen(
            onBackClick = { tampilkanPengaturan = false },
            onLogoutClick = { onLogout() }
        )
    } else if (tampilkanAchievement) {
        AchievementScreen(
            onBackClick = { tampilkanAchievement = false }
        )
    } else if (tampilkanLeaderboard) {
        LeaderboardScreen(
            onBackClick = { tampilkanLeaderboard = false }
        )
    } else {
        // LAYAR UTAMA DENGAN BOTTOM NAVIGATION
        Box(modifier = Modifier.fillMaxSize()) {

            // LAYER 1: KONTEN HALAMAN BERDASARKAN TAB AKTIF
            when (indexAktif) {
                0 -> BerandaScreen(
                    onNavigateToMateri = {
                        // Contoh: Ketika tombol di beranda diklik, langsung buka topik Biologi
                        topikAktif = MateriRepository.getDaftarTopik().firstOrNull()
                    }
                )

                1 -> MateriScreen(
                    onMateriClick = { judulKategori ->
                        // Mencocokkan kartu materi yang diklik dengan data di repository
                        val topikDitemukan = MateriRepository.getDaftarTopik().find {
                            it.judul.contains(judulKategori, ignoreCase = true) ||
                                    it.kategori.contains(judulKategori, ignoreCase = true)
                        }

                        if (topikDitemukan != null) {
                            topikAktif = topikDitemukan
                        } else {
                            // Fallback jika belum ada datanya, buka topik pertama
                            topikAktif = MateriRepository.getDaftarTopik().firstOrNull()
                        }
                    }
                )

                2 -> ProfilScreen(
                    onNavigateToSettings = { tampilkanPengaturan = true },
                    onNavigateToAchievement = { tampilkanAchievement = true },
                    onNavigateToLeaderboard = { tampilkanLeaderboard = true }
                )
            }

            // LAYER 2: FLOATING BOTTOM NAVIGATION
            FloatingBottomNav(
                selectedIndex = indexAktif,
                onItemSelected = { index ->
                    indexAktif = index
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 30.dp,
                        end = 30.dp,
                        bottom = 50.dp
                    )
            )
        }
    }
}