package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.theme.AccentPurple
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

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
    val metrics = rememberResponsiveMetrics()
    val completionPercent = if (totalQuestions > 0) ((correctCount + wrongCount) * 100 / totalQuestions) else 100

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceOffWhite)
    ) {
        // Latar Belakang Atas Sage Green dengan Ornamen Halus
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (metrics.isShortScreen) 270.dp else 320.dp)
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(SagePrimary)
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .offset(x = (-50).dp, y = (-40).dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = 40.dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )
        }

        ResponsiveContentContainer(maxWidth = 560.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = metrics.horizontalPadding)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onBackClick,
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.22f),
                        modifier = Modifier.size(40.dp)
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

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = "Hasil Kuis",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Score Badge Lingkaran
                Surface(
                    shape = CircleShape,
                    color = SurfaceWhite,
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(if (metrics.isShortScreen) 146.dp else 162.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                            .border(3.dp, AccentPurple.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Your Score",
                                fontSize = 12.5.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$score",
                                    fontSize = if (metrics.isShortScreen) 36.sp else 42.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AccentPurple
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "pt",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurple,
                                    modifier = Modifier.padding(bottom = 7.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Kartu Statistik Grid 2x2
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 22.dp, horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                StatItem(
                                    dotColor = AccentPurple,
                                    value = "$completionPercent%",
                                    label = "Completion"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                StatItem(
                                    dotColor = AccentPurple,
                                    value = "$totalQuestions",
                                    label = "Total Question"
                                )
                            }
                        }

                        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

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

                Spacer(modifier = Modifier.height(36.dp))

                // Tombol Navigasi Bawah
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    BottomActionButton(
                        icon = Icons.Default.Visibility,
                        label = "Review",
                        circleColor = Color(0xFFD97706).copy(alpha = 0.14f),
                        iconTint = Color(0xFFD97706),
                        onClick = onReviewClick,
                        modifier = Modifier.weight(1f)
                    )

                    BottomActionButton(
                        icon = Icons.Default.Home,
                        label = "Home",
                        circleColor = AccentPurple,
                        iconTint = Color.White,
                        isMain = true,
                        onClick = onHomeClick,
                        modifier = Modifier.weight(1f)
                    )

                    BottomActionButton(
                        icon = Icons.Default.BarChart,
                        label = "Leaderboard",
                        circleColor = SurfaceWhite,
                        iconTint = TextPrimary,
                        onClick = onLeaderboardClick,
                        modifier = Modifier.weight(1f)
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
                .size(10.dp)
                .background(dotColor, CircleShape)
        )
        Column {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = circleColor,
            shadowElevation = if (isMain) 6.dp else 2.dp,
            border = if (isMain) null else BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.size(if (isMain) 64.dp else 52.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(if (isMain) 28.dp else 22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
    }
}
