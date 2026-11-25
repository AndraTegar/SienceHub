package com.kelompoksix.siencehub.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.data.repositories.LocalLearningRepository
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.data.repositories.QuizHistoryRepository
import com.kelompoksix.siencehub.data.repositories.UserProgressRepository
import com.kelompoksix.siencehub.ui.components.FloatingBottomNav
import com.kelompoksix.siencehub.ui.screens.AchievementScreen
import com.kelompoksix.siencehub.ui.screens.BabScreen
import com.kelompoksix.siencehub.ui.screens.BerandaScreen
import com.kelompoksix.siencehub.ui.screens.DetailMateriScreen
import com.kelompoksix.siencehub.ui.screens.EditProfilScreen
import com.kelompoksix.siencehub.ui.screens.EksperimenScreen
import com.kelompoksix.siencehub.ui.screens.GravitasiPlanetScreen
import com.kelompoksix.siencehub.ui.screens.LeaderboardScreen
import com.kelompoksix.siencehub.ui.screens.MateriScreen
import com.kelompoksix.siencehub.ui.screens.NotifikasiScreen
import com.kelompoksix.siencehub.ui.screens.ProfilScreen
import com.kelompoksix.siencehub.ui.screens.QuizResultScreen
import com.kelompoksix.siencehub.ui.screens.QuizReviewScreen
import com.kelompoksix.siencehub.ui.screens.QuizScreen
import com.kelompoksix.siencehub.ui.screens.SettingScreen
import com.kelompoksix.siencehub.ui.screens.SimulatorNewtonScreen
import com.kelompoksix.siencehub.ui.screens.TimbanganScreen
import com.kelompoksix.siencehub.ui.screens.UjiPhScreen

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
    onStartQuiz: (String) -> Unit
) {
    val context = LocalContext.current
    val learningStats by LocalLearningRepository.learningStatsFlow.collectAsState()

    LaunchedEffect(context) {
        UserProgressRepository.loadProgress(context)
        QuizHistoryRepository.fetchUserQuizHistory(context)
        LocalLearningRepository.refreshStats(context)
    }

    var indexAktif by remember { mutableIntStateOf(0) }

    var tampilkanEksperimen by remember { mutableStateOf(false) }
    var eksperimenAktif by remember { mutableStateOf<String?>(null) }

    var tampilkanPengaturan by remember { mutableStateOf(false) }
    var tampilkanAchievement by remember { mutableStateOf(false) }
    var tampilkanLeaderboard by remember { mutableStateOf(false) }
    var tampilkanNotifikasi by remember { mutableStateOf(false) }
    var tampilkanEditProfil by remember { mutableStateOf(false) }

    var topikAktif by remember { mutableStateOf<TopikMateri?>(null) }
    var babAktif by remember { mutableStateOf<Int?>(null) }

    var kuisAktif by remember { mutableStateOf<String?>(null) }
    var hasilKuisAktif by remember { mutableStateOf<HasilKuisData?>(null) }
    var reviewAktif by remember { mutableStateOf<String?>(null) }

    val bukaTopik: (TopikMateri, Int?) -> Unit = { topik, idBab ->
        LocalLearningRepository.recordTopicOpened(context = context, topicId = topik.id)
        topikAktif = topik
        babAktif = idBab
    }

    BackHandler(
        enabled = indexAktif != 0 ||
            tampilkanPengaturan ||
            tampilkanAchievement ||
            tampilkanLeaderboard ||
            tampilkanNotifikasi ||
            tampilkanEditProfil ||
            topikAktif != null ||
            kuisAktif != null ||
            hasilKuisAktif != null ||
            reviewAktif != null ||
            tampilkanEksperimen ||
            eksperimenAktif != null
    ) {
        when {
            reviewAktif != null -> reviewAktif = null
            hasilKuisAktif != null -> {
                hasilKuisAktif = null
                kuisAktif = null
            }
            kuisAktif != null -> kuisAktif = null
            babAktif != null -> babAktif = null
            topikAktif != null -> topikAktif = null
            eksperimenAktif != null -> eksperimenAktif = null
            tampilkanEksperimen -> tampilkanEksperimen = false
            tampilkanEditProfil -> tampilkanEditProfil = false
            tampilkanPengaturan -> tampilkanPengaturan = false
            tampilkanAchievement -> tampilkanAchievement = false
            tampilkanLeaderboard -> tampilkanLeaderboard = false
            tampilkanNotifikasi -> tampilkanNotifikasi = false
            else -> indexAktif = 0
        }
    }

    when {
        reviewAktif != null -> {
            QuizReviewScreen(
                subjectName = reviewAktif!!,
                onBackClick = { reviewAktif = null }
            )
        }

        hasilKuisAktif != null -> {
            QuizResultScreen(
                score = hasilKuisAktif!!.score,
                totalQuestions = hasilKuisAktif!!.total / 20,
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
                    hasilKuisAktif = null
                    kuisAktif = null
                    topikAktif = null
                    babAktif = null
                },
                onLeaderboardClick = {
                    hasilKuisAktif = null
                    kuisAktif = null
                    topikAktif = null
                    babAktif = null
                    tampilkanLeaderboard = true
                }
            )
        }

        kuisAktif != null -> {
            QuizScreen(
                subjectName = kuisAktif!!,
                onBackClick = { kuisAktif = null },
                onQuizFinished = { score, total, correct, wrong ->
                    val subjekSelesai = kuisAktif!!
                    kuisAktif = null
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

        topikAktif != null && babAktif != null -> {
            val currentTopic = topikAktif!!
            val currentBabId = babAktif!!
            val completedIds = learningStats.completedChaptersByTopic[currentTopic.id].orEmpty()
            val nextBab = currentTopic.daftarBab.firstOrNull { it.id == currentBabId + 1 }

            BabScreen(
                topikMateri = currentTopic,
                babId = currentBabId,
                onBackClick = { babAktif = null },
                isCompleted = currentBabId in completedIds,
                onCompleteChapter = {
                    LocalLearningRepository.completeChapter(
                        context = context,
                        topicId = currentTopic.id,
                        chapterId = currentBabId
                    )
                },
                onNextBab = if (nextBab != null) {
                    { babAktif = nextBab.id }
                } else {
                    null
                }
            )
        }

        topikAktif != null -> {
            val currentTopic = topikAktif!!
            val completedIds = learningStats.completedChaptersByTopic[currentTopic.id].orEmpty()
            DetailMateriScreen(
                topikMateri = currentTopic,
                onBackClick = { topikAktif = null },
                onBabClick = { idBab -> babAktif = idBab },
                onKuisClick = {
                    kuisAktif = MateriRepository.normalizeSubjectName(currentTopic.kategori)
                },
                completedChapterIds = completedIds
            )
        }

        tampilkanEditProfil -> {
            EditProfilScreen(
                onBackClick = { tampilkanEditProfil = false }
            )
        }

        tampilkanPengaturan -> {
            SettingScreen(
                onBackClick = { tampilkanPengaturan = false },
                onLogoutClick = { onLogout() },
                onNavigateToEditProfile = { tampilkanEditProfil = true }
            )
        }

        tampilkanAchievement -> {
            AchievementScreen(
                learningStats = learningStats,
                onBackClick = { tampilkanAchievement = false }
            )
        }

        tampilkanLeaderboard -> {
            LeaderboardScreen(
                onBackClick = { tampilkanLeaderboard = false }
            )
        }

        tampilkanNotifikasi -> {
            NotifikasiScreen(
                onBackClick = { tampilkanNotifikasi = false }
            )
        }

        eksperimenAktif != null -> {
            when (eksperimenAktif) {
                "fisika_newton2" -> SimulatorNewtonScreen(onBackClick = { eksperimenAktif = null })
                "astronomi_gravitasi" -> GravitasiPlanetScreen(onBackClick = { eksperimenAktif = null })
                "kimia_ph" -> UjiPhScreen(onBackClick = { eksperimenAktif = null })
                "matematika_timbangan" -> TimbanganScreen(onBackClick = { eksperimenAktif = null })
                else -> eksperimenAktif = null
            }
        }

        tampilkanEksperimen -> {
            EksperimenScreen(
                onBackClick = { tampilkanEksperimen = false },
                onPilih = { idEksperimen -> eksperimenAktif = idEksperimen }
            )
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Crossfade(
                    targetState = indexAktif,
                    animationSpec = tween(220),
                    label = "MainTabCrossfade"
                ) { tab ->
                    when (tab) {
                        0 -> BerandaScreen(
                            onNavigateToMateri = { judulMateri ->
                                val topikDitemukan = MateriRepository.findTopicBySubject(judulMateri)
                                    ?: MateriRepository.getDaftarTopik().firstOrNull()
                                if (topikDitemukan != null) {
                                    bukaTopik(topikDitemukan, null)
                                }
                            },
                            onHasilCari = { topik, babId ->
                                bukaTopik(topik, babId)
                            },
                            onNavigateToEksperimen = { tampilkanEksperimen = true },
                            onMulaiKuis = { kategori -> kuisAktif = kategori },
                            onNotificationClick = { tampilkanNotifikasi = true }
                        )

                        1 -> MateriScreen(
                            onMateriClick = { judulKategori ->
                                val topikDitemukan = MateriRepository.findTopicBySubject(judulKategori)
                                    ?: MateriRepository.getDaftarTopik().firstOrNull()
                                if (topikDitemukan != null) {
                                    bukaTopik(topikDitemukan, null)
                                }
                            }
                        )

                        2 -> ProfilScreen(
                            onNavigateToSettings = { tampilkanPengaturan = true },
                            onNavigateToAchievement = { tampilkanAchievement = true },
                            onNavigateToLeaderboard = { tampilkanLeaderboard = true },
                            onNavigateToEditProfile = { tampilkanEditProfil = true }
                        )
                    }
                }

                FloatingBottomNav(
                    selectedIndex = indexAktif,
                    onItemSelected = { index -> indexAktif = index },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }
        }
    }
}
