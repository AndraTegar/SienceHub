package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.QuizHistoryItem
import com.kelompoksix.siencehub.data.repositories.QuizHistoryRepository
import com.kelompoksix.siencehub.utils.UserSessionManager

@Composable
fun ProfilScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAchievement: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {}
) {
    val sageGreen = Color(0xFF7A8B76)
    val lightBlue = Color(0xFFE3F0FF)

    val context = LocalContext.current

    // Ambil data Riwayat Kuis Real-Time dari Cloud Firestore & SharedPreferences
    DisposableEffect(context) {
        QuizHistoryRepository.fetchUserQuizHistory(context)
        onDispose { }
    }

    val quizHistory by QuizHistoryRepository.quizHistoryFlow.collectAsState()

    val userName = remember(context) {
        UserSessionManager.getUserName(context).ifBlank { "Pengguna ScienceHub" }
    }
    val userEmail = remember(context) {
        UserSessionManager.getUserEmail(context).ifBlank { "user@sciencehub.com" }
    }
    val userPhone = remember(context) {
        UserSessionManager.getUserPhone(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.38f).background(sageGreen))

        Surface(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.72f).align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            color = Color(0xFFFAFAFA)
        ) {}

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(48.dp))

                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(105.dp)
                            .border(4.dp, Color.White, CircleShape)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(52.dp), tint = Color.Gray)
                    }

                    // Tombol Pensil Edit Profil
                    IconButton(
                        onClick = onNavigateToEditProfile,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.LightGray, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profil",
                            tint = sageGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onNavigateToEditProfile() }
                ) {
                    Text(text = userName, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                }

                Text(text = userEmail, fontSize = 14.sp, color = Color.White.copy(alpha = 0.85f))

                if (userPhone.isNotBlank()) {
                    Text(text = "📱 $userPhone", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f), modifier = Modifier.padding(top = 2.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Barisan Tombol Melayang
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    MenuKotakFresh(icon = Icons.Default.BarChart, bgColor = lightBlue, onClick = onNavigateToLeaderboard)
                    Spacer(modifier = Modifier.width(20.dp))
                    MenuKotakFresh(icon = Icons.Default.WorkspacePremium, bgColor = lightBlue, onClick = onNavigateToAchievement)
                    Spacer(modifier = Modifier.width(20.dp))
                    MenuKotakFresh(icon = Icons.Default.Settings, bgColor = lightBlue, onClick = onNavigateToSettings)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Kartu Level & Streak
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadgeFresh(icon = Icons.Default.Star, value = "${(quizHistory.sumOf { it.score } / 100) + 1}", label = "Level", iconColor = Color(0xFF00B0FF), bgColor = Color(0xFFE1F5FE))
                        Box(modifier = Modifier.height(45.dp).width(1.5.dp).background(Color.LightGray.copy(alpha = 0.5f)))
                        StatusBadgeFresh(icon = Icons.Default.LocalFireDepartment, value = "6", label = "Streak", iconColor = Color(0xFFFF6D00), bgColor = Color(0xFFFFF3E0))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Header Riwayat Kuis
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Riwayat & Hasil Kuis 📊", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Text(text = "${quizHistory.size} Selesai", fontSize = 12.sp, color = sageGreen, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (quizHistory.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFE8ECE7), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = sageGreen)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Belum ada kuis yang diikuti", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Selesaikan kuis untuk melihat riwayat nilai di sini.", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            } else {
                items(quizHistory) { item ->
                    QuizHistoryCardItem(item = item)
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun QuizHistoryCardItem(item: QuizHistoryItem) {
    val sageGreen = Color(0xFF7A8B76)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFE8ECE7), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = sageGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = item.subjectName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${item.correctCount} Benar • ${item.wrongCount} Salah • ${item.formattedDate}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF1F5F9)
            ) {
                Text(
                    text = "${item.score} pt",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8B4CFC),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun MenuKotakFresh(icon: androidx.compose.ui.graphics.vector.ImageVector, bgColor: Color, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp), color = bgColor, shadowElevation = 4.dp,
        modifier = Modifier.size(75.dp).clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF556052), modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun StatusBadgeFresh(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, iconColor: Color, bgColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(48.dp).background(bgColor, CircleShape), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color.Black)
            Text(text = label, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}
