package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.LearningStats
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

private data class AchievementItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBackground: Color,
    val isComplete: Boolean
)

@Composable
fun AchievementScreen(
    learningStats: LearningStats = LearningStats.EMPTY,
    onBackClick: () -> Unit
) {
    val metrics = rememberResponsiveMetrics()
    val achievements = listOf(
        AchievementItem(
            "Langkah Pertama",
            "Menyelesaikan 1 bab materi.",
            Icons.Default.Star,
            Color(0xFFF59E0B),
            Color(0xFFFFFBEB),
            learningStats.completedChapterCount >= 1
        ),
        AchievementItem(
            "Si Paling Rajin",
            "Mencapai 5 hari streak belajar berturut-turut.",
            Icons.Default.LocalFireDepartment,
            Color(0xFFEA580C),
            Color(0xFFFFF7ED),
            learningStats.streakDays >= 5
        ),
        AchievementItem(
            "Ilmuwan Gila",
            "Mendapatkan nilai sempurna untuk kuis Biologi dan Kimia.",
            Icons.Default.Science,
            SagePrimary,
            SageSoft,
            setOf("biologi", "kimia").all { it in learningStats.perfectQuizSubjects }
        ),
        AchievementItem(
            "Raja Kuis",
            "Menjawab total 50 soal kuis dengan benar.",
            Icons.Default.EmojiEvents,
            Color(0xFFD97706),
            Color(0xFFFFF8E1),
            learningStats.correctQuizAnswers >= 50
        )
    )
    val completedAchievements = achievements.filter { it.isComplete }
    val lockedAchievements = achievements.filterNot { it.isComplete }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    onClick = onBackClick,
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Kembali",
                            tint = SagePrimary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Pencapaian Kamu",
                    color = Color.White,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "${completedAchievements.size}/${achievements.size}",
                    color = Color.White,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SurfaceOffWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 680.dp) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(
                        horizontal = metrics.horizontalPadding,
                        vertical = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "Terselesaikan \uD83C\uDF1F",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SagePrimary
                        )
                    }

                    if (completedAchievements.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Belum ada pencapaian terbuka. Terus belajar untuk membukanya!",
                                    color = TextSecondary,
                                    fontSize = 13.5.sp,
                                    modifier = Modifier.padding(18.dp)
                                )
                            }
                        }
                    }

                    items(completedAchievements) { achievement ->
                        KartuAchievement(
                            judul = achievement.title,
                            deskripsi = achievement.description,
                            ikon = achievement.icon,
                            warnaIkon = achievement.iconColor,
                            warnaBgIkon = achievement.iconBackground,
                            sudahSelesai = true
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(color = BorderSubtle)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Belum Terbuka \uD83D\uDD12",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }

                    items(lockedAchievements) { achievement ->
                        KartuAchievement(
                            judul = achievement.title,
                            deskripsi = achievement.description,
                            ikon = achievement.icon,
                            warnaIkon = TextSecondary,
                            warnaBgIkon = Color(0xFFEAECEF),
                            sudahSelesai = false
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun KartuAchievement(
    judul: String,
    deskripsi: String,
    ikon: ImageVector,
    warnaIkon: Color,
    warnaBgIkon: Color,
    sudahSelesai: Boolean
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (sudahSelesai) 2.dp else 0.dp),
        border = BorderStroke(1.dp, if (sudahSelesai) SagePrimary.copy(alpha = 0.25f) else BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (sudahSelesai) 1f else 0.72f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(warnaBgIkon, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (sudahSelesai) ikon else Icons.Default.Lock,
                    contentDescription = null,
                    tint = warnaIkon,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = judul,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = deskripsi,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
