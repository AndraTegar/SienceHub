package com.kelompoksix.siencehub.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.LearningStats

private data class AchievementItem(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color,
    val iconBackground: Color,
    val isComplete: Boolean
)

@Composable
fun AchievementScreen(
    learningStats: LearningStats = LearningStats.EMPTY,
    onBackClick: () -> Unit
) {
    val sageGreen = Color(0xFF7A8B76)
    val achievements = listOf(
        AchievementItem(
            "Langkah Pertama",
            "Menyelesaikan 1 bab materi.",
            Icons.Default.Star,
            Color(0xFFFFD54F),
            Color(0xFFFFF8E1),
            learningStats.completedChapterCount >= 1
        ),
        AchievementItem(
            "Si Paling Rajin",
            "Mencapai 5 hari streak belajar berturut-turut.",
            Icons.Default.LocalFireDepartment,
            Color(0xFFFF8A65),
            Color(0xFFFBE9E7),
            learningStats.streakDays >= 5
        ),
        AchievementItem(
            "Ilmuwan Gila",
            "Mendapatkan nilai sempurna untuk kuis Biologi dan Kimia.",
            Icons.Default.Science,
            Color(0xFF7A8B76),
            Color(0xFFE8ECE7),
            setOf("biologi", "kimia").all { it in learningStats.perfectQuizSubjects }
        ),
        AchievementItem(
            "Raja Kuis",
            "Menjawab total 50 soal kuis dengan benar.",
            Icons.Default.EmojiEvents,
            Color(0xFFFFB300),
            Color(0xFFFFF8E1),
            learningStats.correctQuizAnswers >= 50
        )
    )
    val completedAchievements = achievements.filter { it.isComplete }
    val lockedAchievements = achievements.filterNot { it.isComplete }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(sageGreen)
    ) {
        // ==========================================
        // HEADER PENCAPAIAN
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(50))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Kembali",
                    tint = sageGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Pencapaian Kamu",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ==========================================
        // KONTEN DAFTAR PENCAPAIAN (Background Putih)
        // ==========================================
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFAFAFA),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Terselesaikan \uD83C\uDF1F",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = sageGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (completedAchievements.isEmpty()) {
                    item {
                        Text("Belum ada pencapaian. Terus belajar untuk membukanya!", color = Color.Gray)
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray.copy(alpha = 0.5f)))
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "Belum Terbuka \uD83D\uDD12",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(lockedAchievements) { achievement ->
                    KartuAchievement(
                        judul = achievement.title,
                        deskripsi = achievement.description,
                        ikon = achievement.icon,
                        warnaIkon = Color.Gray,
                        warnaBgIkon = Color.LightGray,
                        sudahSelesai = false
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp)) // Jarak bawah
                }
            }
        }
    }
}

@Composable
fun KartuAchievement(
    judul: String,
    deskripsi: String,
    ikon: androidx.compose.ui.graphics.vector.ImageVector,
    warnaIkon: Color,
    warnaBgIkon: Color,
    sudahSelesai: Boolean
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (sudahSelesai) 4.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (sudahSelesai) 1f else 0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(warnaBgIkon, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (sudahSelesai) ikon else Icons.Default.Lock,
                    contentDescription = null,
                    tint = warnaIkon,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = judul,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = deskripsi,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}