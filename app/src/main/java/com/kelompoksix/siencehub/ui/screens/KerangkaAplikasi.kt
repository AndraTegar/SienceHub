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

    // Tombol Back Android Handler (Menutup overlay dulu atau kembali ke beranda)
    BackHandler(enabled = indexAktif != 0 || tampilkanPengaturan || tampilkanAchievement || tampilkanLeaderboard) {
        when {
            tampilkanPengaturan -> tampilkanPengaturan = false
            tampilkanAchievement -> tampilkanAchievement = false
            tampilkanLeaderboard -> tampilkanLeaderboard = false
            else -> indexAktif = 0
        }
    }

    // Menampilkan layar penuh (overlay) jika salah satu menu khusus dipilih
    if (tampilkanPengaturan) {
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
                    onNavigateToMateri = { indexAktif = 1 }
                )

                1 -> MateriScreen()

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