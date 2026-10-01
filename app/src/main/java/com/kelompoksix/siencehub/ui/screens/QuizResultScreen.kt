package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int = 20,
    correctCount: Int = 13,
    wrongCount: Int = 7,
    onReviewClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val sageGreen = Color(0xFF7A8B76)
    val purplePrimary = Color(0xFF8B4CFC)
    val completionPercent = if (totalQuestions > 0) ((correctCount + wrongCount) * 100 / totalQuestions) else 100

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF9))
    ) {
        // 1. Background Atas (Sage Green dengan Pattern Lingkaran Dekoratif)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
                .background(sageGreen)
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .offset(x = (-50).dp, y = (-40).dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 50.dp, y = 40.dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )

            // Top Bar: Tombol Kembali & Judul Halaman
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { onBackClick() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Hasil Kuis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // 2. Konten Utama Berbentuk Kolom
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(76.dp))

            // Score Badge Lingkaran Besar
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 10.dp,
                modifier = Modifier.size(165.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                        .border(3.dp, Color(0xFFF1F5F9), CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Your Score",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$score",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = purplePrimary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "pt",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = purplePrimary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Kartu Statistik Grid 2x2 (Menggunakan modifier weight(1f) agar simetris sempurna)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 22.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Baris 1: Completion & Total Question
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            StatItem(
                                dotColor = purplePrimary,
                                value = "$completionPercent%",
                                label = "Completion"
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            StatItem(
                                dotColor = purplePrimary,
                                value = "$totalQuestions",
                                label = "Total Question"
                            )
                        }
                    }

                    // Garis Pemisah Tipis
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                    // Baris 2: Correct & Wrong (Sudah dipastikan sejajar presisi)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            StatItem(
                                dotColor = Color(0xFF22C55E),
                                value = String.format("%02d", correctCount),
                                label = "Correct"
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            StatItem(
                                dotColor = Color(0xFFEF4444),
                                value = String.format("%02d", wrongCount),
                                label = "Wrong"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Navigasi Bawah
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.width(95.dp), contentAlignment = Alignment.Center) {
                    BottomActionButton(
                        icon = Icons.Default.Visibility,
                        label = "Review",
                        circleColor = Color(0xFFFFF7ED),
                        iconTint = Color(0xFFD97706),
                        onClick = onReviewClick
                    )
                }

                Box(modifier = Modifier.width(95.dp), contentAlignment = Alignment.Center) {
                    BottomActionButton(
                        icon = Icons.Default.Home,
                        label = "Home",
                        circleColor = purplePrimary,
                        iconTint = Color.White,
                        isMain = true,
                        onClick = onHomeClick
                    )
                }

                Box(modifier = Modifier.width(95.dp), contentAlignment = Alignment.Center) {
                    BottomActionButton(
                        icon = Icons.Default.BarChart,
                        label = "Leaderboard",
                        circleColor = Color(0xFFF1F5F9),
                        iconTint = Color(0xFF475569),
                        onClick = onLeaderboardClick
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    dotColor: Color,
    value: String,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(dotColor, CircleShape)
        )
        Column {
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun BottomActionButton(
    icon: ImageVector,
    label: String,
    circleColor: Color,
    iconTint: Color,
    isMain: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = circleColor,
            shadowElevation = if (isMain) 6.dp else 2.dp,
            modifier = Modifier.size(if (isMain) 66.dp else 52.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(if (isMain) 30.dp else 22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
        )
    }
}