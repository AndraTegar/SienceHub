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
import com.kelompoksix.siencehub.ui.components.BacaMateriScreen
import com.kelompoksix.siencehub.ui.components.FloatingBottomNav

@Composable
fun KerangkaAplikasi(
    isDarkMode: Boolean = false,
    onDarkModeChanged: (Boolean) -> Unit = {},
    onLogout: () -> Unit
) {
    var indexAktif by remember { mutableIntStateOf(0) }

    var tampilkanPengaturan by remember { mutableStateOf(false) }
    var tampilkanAchievement by remember { mutableStateOf(false) }
    var tampilkanLeaderboard by remember { mutableStateOf(false) }

    var topikAktif by remember { mutableStateOf<TopikMateri?>(null) }
    var bacaBabAktif by remember { mutableStateOf<KontenBab?>(null) }

    BackHandler(enabled = indexAktif != 0 || tampilkanPengaturan || tampilkanAchievement || tampilkanLeaderboard || topikAktif != null || bacaBabAktif != null) {
        when {
            bacaBabAktif != null -> bacaBabAktif = null
            topikAktif != null -> topikAktif = null
            tampilkanPengaturan -> tampilkanPengaturan = false
            tampilkanAchievement -> tampilkanAchievement = false
            tampilkanLeaderboard -> tampilkanLeaderboard = false
            else -> indexAktif = 0
        }
    }

    if (bacaBabAktif != null) {
        BacaMateriScreen(
            konten = bacaBabAktif!!,
            onBackClick = { bacaBabAktif = null },
            onMulaiKuisClick = { idBab ->
                println("Tombol Kuis Bab $idBab ditekan!")
            }
        )
    } else if (topikAktif != null) {
        DetailMateriScreen(
            topikMateri = topikAktif!!,
            onBackClick = { topikAktif = null },
            onBabClick = { idBab ->
                val isiBab = IsiMateriRepository.getBabById(idBab)
                if (isiBab != null) {
                    bacaBabAktif = isiBab
                }
            }
        )
    } else if (tampilkanPengaturan) {
        SettingScreen(
            isDarkMode = isDarkMode,
            onDarkModeChanged = onDarkModeChanged,
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
        Box(modifier = Modifier.fillMaxSize()) {
            when (indexAktif) {
                0 -> BerandaScreen(
                    onNavigateToMateri = {
                        topikAktif = MateriRepository.getDaftarTopik().firstOrNull()
                    }
                )

                1 -> MateriScreen(
                    onMateriClick = { judulKategori ->
                        val topikDitemukan = MateriRepository.getDaftarTopik().find {
                            it.judul.contains(judulKategori, ignoreCase = true) ||
                                    it.kategori.contains(judulKategori, ignoreCase = true)
                        }

                        if (topikDitemukan != null) {
                            topikAktif = topikDitemukan
                        } else {
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
