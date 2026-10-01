package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    onLeaderboardClick: () -> Unit = {}
) {
    val sageGreen = Color(0xFF859585)
    val purplePrimary = Color(0xFF8B4CFC)
    val completionPercent = if (totalQuestions > 0) ((correctCount + wrongCount) * 100 / totalQuestions) else 100

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Background Atas (Sage Green dengan Pattern Lingkaran)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .background(sageGreen)
        ) {
            // Ornamen Dekoratif Lingkaran Latar Belakang
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .offset(x = (-40).dp, y = (-20).dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = 30.dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Score Badge Lingkaran
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(170.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .border(3.dp, Color.White, CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "your Score",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$score",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = purplePrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "pt",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = purplePrimary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Kartu Statistik Grid 2x2
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Baris 1: Completion & Total Question
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(
                            dotColor = purplePrimary,
                            value = "$completionPercent%",
                            label = "Completion"
                        )
                        StatItem(
                            dotColor = purplePrimary,
                            value = "$totalQuestions",
                            label = "Total Question"
                        )
                    }

                    // Baris 2: Correct & Wrong
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(
                            dotColor = Color(0xFF22C55E),
                            value = String.format("%02d", correctCount),
                            label = "Correct"
                        )
                        StatItem(
                            dotColor = Color(0xFFEF4444),
                            value = String.format("%02d", wrongCount),
                            label = "Wrong"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Navigasi Bawah (Review Answer, Home, Leaderboard)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomActionButton(
                    icon = Icons.Default.Visibility,
                    label = "Review Answer",
                    circleColor = Color(0xFFE2E8F0),
                    iconTint = Color(0xFFD97706),
                    onClick = onReviewClick
                )

                BottomActionButton(
                    icon = Icons.Default.Home,
                    label = "Home",
                    circleColor = purplePrimary,
                    iconTint = Color.White,
                    isMain = true,
                    onClick = onHomeClick
                )

                BottomActionButton(
                    icon = Icons.Default.BarChart,
                    label = "Leaderboard",
                    circleColor = Color(0xFFE2E8F0),
                    iconTint = Color(0xFF475569),
                    onClick = onLeaderboardClick
                )
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
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp, end = 8.dp)
                .size(10.dp)
                .background(dotColor, CircleShape)
        )
        Column {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = label,
                fontSize = 12.sp,
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
            modifier = Modifier.size(if (isMain) 64.dp else 52.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(if (isMain) 30.dp else 24.dp)
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