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
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.ui.components.FloatingBottomNav

// Data class penampung hasil kuis untuk dilempar ke Result & Review Screen
data class HasilKuisData(
    val subjectName: String,
    val score: Int,
    val total: Int,
    val correct: Int,
    val wrong: Int
)

@Composable
fun KerangkaAplikasi(
    onLogout: () -> Unit,
    onStartQuiz: (String) -> Unit // <-- Menggunakan callback kuis
) {
    var indexAktif by remember { mutableIntStateOf(0) }

    //experimen fisika
    var tampilkanEksperimen by remember { mutableStateOf(false) }
    var eksperimenAktif by remember { mutableStateOf<String?>(null) }

    // Status untuk halaman tambahan yang menutupi Navbar (Overlay Screen)
    var tampilkanPengaturan by remember { mutableStateOf(false) }
    var tampilkanAchievement by remember { mutableStateOf(false) }
    var tampilkanLeaderboard by remember { mutableStateOf(false) }

    // Status untuk menyimpan topik materi yang sedang dibuka
    var topikAktif by remember { mutableStateOf<TopikMateri?>(null) }
    var babAktif by remember { mutableStateOf<Int?>(null) }

    // VARIABEL KUIS, RESULT, DAN REVIEW
    var kuisAktif by remember { mutableStateOf<String?>(null) }
    var hasilKuisAktif by remember { mutableStateOf<HasilKuisData?>(null) }
    var reviewAktif by remember { mutableStateOf<String?>(null) }

    // Tombol Back Android Handler (Menutup overlay, review, hasil, kuis, atau materi secara bertahap)
    BackHandler(enabled = indexAktif != 0 || tampilkanPengaturan || tampilkanAchievement || tampilkanLeaderboard || topikAktif != null || kuisAktif != null || hasilKuisAktif != null || reviewAktif != null) {
        when {
            babAktif != null -> babAktif = null
            topikAktif != null -> topikAktif = null
            eksperimenAktif != null -> eksperimenAktif = null
            tampilkanEksperimen -> tampilkanEksperimen = false
            tampilkanPengaturan -> tampilkanPengaturan = false
            tampilkanAchievement -> tampilkanAchievement = false
            tampilkanLeaderboard -> tampilkanLeaderboard = false
            else -> indexAktif = 0
        }
    }

    // Menampilkan layar penuh (overlay / detail / kuis / hasil / review) berdasarkan prioritas
    when {
        // 1. Jika Sedang Melihat Ulasan Soal (QuizReviewScreen)
        reviewAktif != null -> {
            QuizReviewScreen(
                subjectName = reviewAktif!!,
                onBackClick = { reviewAktif = null }
            )
        }

        // 2. Jika Kuis Selesai dan Menampilkan Hasil (QuizResultScreen)
        hasilKuisAktif != null -> {
            QuizResultScreen(
                score = hasilKuisAktif!!.score,
                totalQuestions = hasilKuisAktif!!.total / 20, // Sesuaikan jika per soal bernilai 20 poin
                correctCount = hasilKuisAktif!!.correct,
                wrongCount = hasilKuisAktif!!.wrong,
                onBackClick = {
                    hasilKuisAktif = null
                    kuisAktif = null
                },
                onReviewClick = {
                    reviewAktif = hasilKuisAktif!!.subjectName
                },
                onHomeClick = {
                    // BERSIHKAN SEMUA STATE KUIS AGAR KEMBALI KE BERANDA/MATERI UTAMA
                    hasilKuisAktif = null
                    kuisAktif = null
                    topikAktif = null
                    babAktif = null
                },
                onLeaderboardClick = {
                    // BERSIHKAN STATE KUIS DAN AKTIFKAN LEADERBOARD SECARA LANGSUNG
                    hasilKuisAktif = null
                    kuisAktif = null
                    topikAktif = null
                    babAktif = null
                    tampilkanLeaderboard = true // Langsung buka leaderboard
                }
            )
        }
        // 3. Jika Kuis Sedang Berjalan (QuizScreen)
        kuisAktif != null -> {
            QuizScreen(
                subjectName = kuisAktif!!,
                onBackClick = { kuisAktif = null },
                onQuizFinished = { score, total, correct, wrong ->
                    val subjekSelesai = kuisAktif!!
                    // Tutup kuis yang sedang berjalan
                    kuisAktif = null
                    // Buka layar hasil kuis dengan membawa data skor
                    hasilKuisAktif = HasilKuisData(
                        subjectName = subjekSelesai,
                        score = score,
                        total = total,
                        correct = correct,
                        wrong = wrong
                    )
                }
            )
        }

        // 4. Jika Sedang Membaca Bab Materi
        topikAktif != null && babAktif != null -> {
            BabScreen(
                topikMateri = topikAktif!!,
                babId = babAktif!!,
                onBackClick = { babAktif = null },
            )
        }

        // 5. Jika Sedang di Daftar Bab / Detail Materi
        topikAktif != null -> {
            DetailMateriScreen(
                topikMateri = topikAktif!!,
                onBackClick = { topikAktif = null },
                onBabClick = { idBab -> babAktif = idBab },
                onKuisClick = {
                    // Membuka kuis berdasarkan kategori materi (contoh: "Fisika", "Biologi", dll)
                    kuisAktif = topikAktif!!.kategori
                }
            )
        }

        // 6. Layar Pengaturan
        tampilkanPengaturan -> {
            SettingScreen(
                onBackClick = { tampilkanPengaturan = false },
                onLogoutClick = { onLogout() }
            )
        }

        // 7. Layar Achievement
        tampilkanAchievement -> {
            AchievementScreen(
                onBackClick = { tampilkanAchievement = false }
            )
        }

        // 8. Layar Leaderboard
        tampilkanLeaderboard -> {
            LeaderboardScreen(
                onBackClick = { tampilkanLeaderboard = false }
            )
        }

        // 9. LAYAR UTAMA DENGAN BOTTOM NAVIGATION
        else -> {
            Box(modifier = Modifier.fillMaxSize()) {

            // LAYER 1: KONTEN HALAMAN BERDASARKAN TAB AKTIF
            when (indexAktif) {
                0 -> BerandaScreen(
                    onNavigateToMateri = { judulMateri ->
                        val topikDitemukan = MateriRepository.getDaftarTopik().find {
                            it.judul.equals(judulMateri, ignoreCase = true) ||
                                    it.judul.contains(judulMateri, ignoreCase = true) ||
                                    it.kategori.contains(judulMateri, ignoreCase = true)
                        }

                        if (topikDitemukan != null) {
                            topikAktif = topikDitemukan
                        } else {
                            topikAktif = MateriRepository.getDaftarTopik().firstOrNull()
                        }
                    },
                    onHasilCari = { topik, babId ->
                        topikAktif = topik
                        babAktif = babId
                    },
                    onNavigateToEksperimen = { tampilkanEksperimen = true }   // baru
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