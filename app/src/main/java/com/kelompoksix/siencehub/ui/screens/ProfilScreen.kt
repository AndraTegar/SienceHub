package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.QuizHistoryItem
import com.kelompoksix.siencehub.data.repositories.LocalLearningRepository
import com.kelompoksix.siencehub.data.repositories.QuizHistoryRepository
import com.kelompoksix.siencehub.ui.theme.AccentPurple
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SageDark
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import com.kelompoksix.siencehub.utils.AppSettingsManager
import com.kelompoksix.siencehub.utils.UserSessionManager

@Composable
fun ProfilScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAchievement: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {}
) {
    val lightBlue = if (AppSettingsManager.isDarkMode) Color(0xFF1E2B3A) else Color(0xFFE5F0FF)
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()

    LaunchedEffect(context) {
        QuizHistoryRepository.fetchUserQuizHistory(context)
        LocalLearningRepository.refreshStats(context)
    }

    val quizHistory by QuizHistoryRepository.quizHistoryFlow.collectAsState()
    val learningStats by LocalLearningRepository.learningStatsFlow.collectAsState()

    val userName = UserSessionManager.getUserName(context).ifBlank { "Pengguna ScienceHub" }
    val userEmail = UserSessionManager.getUserEmail(context).ifBlank { "user@sciencehub.com" }
    val userPhone = UserSessionManager.getUserPhone(context)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceOffWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SagePrimary)
            ) {
                ResponsiveContentContainer(maxWidth = 720.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(top = 20.dp, bottom = 36.dp, start = 24.dp, end = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(98.dp)
                                    .border(3.5.dp, Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.88f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = SagePrimary
                                )
                            }

                            Surface(
                                onClick = onNavigateToEditProfile,
                                shape = CircleShape,
                                color = Color.White,
                                shadowElevation = 4.dp,
                                border = BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Profil",
                                        tint = SagePrimary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigateToEditProfile() }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = userName,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        Text(
                            text = "${learningStats.levelTitle} • ${learningStats.experiencePoints} XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.92f)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = userEmail,
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.82f)
                        )

                        if (userPhone.isNotBlank()) {
                            Text(
                                text = "📱 $userPhone",
                                fontSize = 12.5.sp,
                                color = Color.White.copy(alpha = 0.82f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(SurfaceOffWhite)
                )
            }
        }

        // Menu Cepat + Statistik Real-Time
        item {
            ResponsiveContentContainer(maxWidth = 720.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = metrics.horizontalPadding)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
                    ) {
                        MenuKotakFresh(
                            icon = Icons.Default.BarChart,
                            label = "Peringkat",
                            bgColor = lightBlue,
                            onClick = onNavigateToLeaderboard,
                            modifier = Modifier.weight(1f)
                        )
                        MenuKotakFresh(
                            icon = Icons.Default.WorkspacePremium,
                            label = "Pencapaian",
                            bgColor = lightBlue,
                            onClick = onNavigateToAchievement,
                            modifier = Modifier.weight(1f)
                        )
                        MenuKotakFresh(
                            icon = Icons.Default.Settings,
                            label = "Pengaturan",
                            bgColor = lightBlue,
                            onClick = onNavigateToSettings,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Kartu Level, Streak & Bab Selesai Real-Time
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp, horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusBadgeFresh(
                                icon = Icons.Default.Star,
                                value = "${learningStats.level}",
                                label = "Level",
                                iconColor = Color(0xFF0288D1),
                                bgColor = Color(0xFFE1F5FE)
                            )
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp)
                                    .background(BorderSubtle)
                            )
                            StatusBadgeFresh(
                                icon = Icons.Default.LocalFireDepartment,
                                value = "${learningStats.streakDays}",
                                label = "Streak",
                                iconColor = Color(0xFFFF6D00),
                                bgColor = Color(0xFFFFF3E0)
                            )
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp)
                                    .background(BorderSubtle)
                            )
                            StatusBadgeFresh(
                                icon = Icons.AutoMirrored.Filled.MenuBook,
                                value = "${learningStats.completedChapterCount}",
                                label = "Bab",
                                iconColor = SagePrimary,
                                bgColor = SageSoft
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Header Riwayat Kuis
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Riwayat & Hasil Kuis 📊",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Surface(
                            shape = CircleShape,
                            color = SageSoft
                        ) {
                            Text(
                                text = "${quizHistory.size} Selesai",
                                fontSize = 11.5.sp,
                                color = SageDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        if (quizHistory.isEmpty()) {
            item {
                ResponsiveContentContainer(maxWidth = 720.dp) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = metrics.horizontalPadding, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(SageSoft, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = SagePrimary)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Belum ada kuis yang diikuti",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Selesaikan kuis untuk melihat riwayat nilai di sini.",
                                    fontSize = 12.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        } else {
            items(quizHistory) { item ->
                ResponsiveContentContainer(maxWidth = 720.dp) {
                    QuizHistoryCardItem(
                        item = item,
                        modifier = Modifier.padding(horizontal = metrics.horizontalPadding, vertical = 5.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(112.dp))
        }
    }
}

@Composable
fun QuizHistoryCardItem(
    item: QuizHistoryItem,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(SageSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = SagePrimary,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.subjectName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${item.correctCount} Benar • ${item.wrongCount} Salah • ${item.formattedDate}",
                        fontSize = 11.5.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = AccentPurple.copy(alpha = 0.14f)
            ) {
                Text(
                    text = "${item.score} pt",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentPurple,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun MenuKotakFresh(
    icon: ImageVector,
    bgColor: Color,
    onClick: () -> Unit,
    label: String = "",
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier.height(78.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label.ifEmpty { null },
                tint = SageDark,
                modifier = Modifier.size(26.dp)
            )
            if (label.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SageDark,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun StatusBadgeFresh(
    icon: ImageVector,
    value: String,
    label: String,
    iconColor: Color,
    bgColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(21.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 19.sp,
                color = TextPrimary
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
