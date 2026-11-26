package com.kelompoksix.siencehub.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.ui.theme.AccentPurple
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SageDark
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.StatusDanger
import com.kelompoksix.siencehub.ui.theme.StatusSuccess
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int = 20,
    correctCount: Int = 13,
    wrongCount: Int = 7,
    subjectName: String = "",
    onReviewClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val metrics = rememberResponsiveMetrics()
    val safeTotal = totalQuestions.coerceAtLeast(1)
    val accuracyFraction = (correctCount.toFloat() / safeTotal.toFloat()).coerceIn(0f, 1f)
    val accuracyPercent = (accuracyFraction * 100f).roundToInt()
    val displaySubject = remember(subjectName) {
        if (subjectName.isBlank()) "" else MateriRepository.normalizeSubjectName(subjectName)
    }

    val statusLabel = when {
        accuracyPercent >= 85 -> "Sangat Baik"
        accuracyPercent >= 65 -> "Cukup Baik"
        accuracyPercent >= 45 -> "Perlu Evaluasi"
        else -> "Perlu Latihan"
    }

    // Animasi bertahap yang halus dan responsif
    val badgeScale = remember { Animatable(0.86f) }
    val badgeAlpha = remember { Animatable(0f) }
    val countProgress = remember { Animatable(0f) }
    val cardSlide = remember { Animatable(22f) }
    val cardAlpha = remember { Animatable(0f) }
    val actionsSlide = remember { Animatable(18f) }
    val actionsAlpha = remember { Animatable(0f) }

    LaunchedEffect(score, totalQuestions, correctCount) {
        launch {
            badgeAlpha.animateTo(1f, tween(280, easing = FastOutSlowInEasing))
        }
        launch {
            badgeScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        launch {
            delay(80)
            countProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 720, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(120)
            launch { cardAlpha.animateTo(1f, tween(320, easing = FastOutSlowInEasing)) }
            launch { cardSlide.animateTo(0f, tween(360, easing = FastOutSlowInEasing)) }
        }
        launch {
            delay(210)
            launch { actionsAlpha.animateTo(1f, tween(300, easing = FastOutSlowInEasing)) }
            launch { actionsSlide.animateTo(0f, tween(340, easing = FastOutSlowInEasing)) }
        }
    }

    val animatedScore = (score * countProgress.value).roundToInt()
    val animatedAccuracy = (accuracyPercent * countProgress.value).roundToInt()
    val animatedCorrect = (correctCount * countProgress.value).roundToInt()
    val animatedWrong = (wrongCount * countProgress.value).roundToInt()
    val animatedArcRatio = accuracyFraction * countProgress.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceOffWhite)
    ) {
        // Latar Belakang Atas Sage Green dengan Ornamen Halus
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (metrics.isShortScreen) 268.dp else 312.dp)
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(SagePrimary)
        ) {
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .offset(x = (-48).dp, y = (-42).dp)
                    .background(Color.White.copy(alpha = 0.07f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 38.dp, y = 42.dp)
                    .background(Color.White.copy(alpha = 0.07f), CircleShape)
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
                        color = Color.White.copy(alpha = 0.20f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.26f)),
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

                    Column {
                        Text(
                            text = "Hasil Kuis",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (displaySubject.isNotBlank()) {
                            Text(
                                text = displaySubject,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.84f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Score Badge Lingkaran dengan Cincin Progres Akurasi Animasi
                val badgeSize = if (metrics.isShortScreen) 154.dp else 172.dp
                Surface(
                    shape = CircleShape,
                    color = SurfaceWhite,
                    shadowElevation = 8.dp,
                    border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .size(badgeSize)
                        .graphicsLayer {
                            scaleX = badgeScale.value
                            scaleY = badgeScale.value
                            alpha = badgeAlpha.value
                        }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val ringTrackColor = AccentPurple.copy(alpha = 0.13f)
                        val ringColor = AccentPurple
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(11.dp)
                        ) {
                            val strokePx = 6.dp.toPx()
                            drawCircle(
                                color = ringTrackColor,
                                style = Stroke(width = strokePx)
                            )
                            if (animatedArcRatio > 0.005f) {
                                drawArc(
                                    color = ringColor,
                                    startAngle = -90f,
                                    sweepAngle = 360f * animatedArcRatio,
                                    useCenter = false,
                                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Your Score",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$animatedScore",
                                    fontSize = if (metrics.isShortScreen) 36.sp else 42.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AccentPurple
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "pt",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurple,
                                    modifier = Modifier.padding(bottom = 7.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Surface(
                                shape = CircleShape,
                                color = SageSoft
                            ) {
                                Text(
                                    text = statusLabel,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SageDark,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Kartu Statistik Grid 2x2 + Bar Proporsi Jawaban
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = cardAlpha.value
                            translationY = cardSlide.value.dp.toPx()
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 22.dp, horizontal = 22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Bar rasio Benar vs Salah/Kosong yang teranimasi halus
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ringkasan Hasil",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "$correctCount dari $totalQuestions benar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(CircleShape)
                                    .background(StatusDanger.copy(alpha = 0.16f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(animatedArcRatio.coerceIn(0f, 1f))
                                        .clip(CircleShape)
                                        .background(StatusSuccess)
                                )
                            }
                        }

                        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                StatItem(
                                    dotColor = AccentPurple,
                                    value = "$animatedAccuracy%",
                                    label = "Akurasi"
                                )
                            }
                            VerticalDivider(
                                color = BorderSubtle,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 12.dp)
                            )
                            Box(modifier = Modifier.weight(1f)) {
                                StatItem(
                                    dotColor = SagePrimary,
                                    value = "$totalQuestions",
                                    label = "Total Soal"
                                )
                            }
                        }

                        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                StatItem(
                                    dotColor = StatusSuccess,
                                    value = String.format("%02d", animatedCorrect),
                                    label = "Benar"
                                )
                            }
                            VerticalDivider(
                                color = BorderSubtle,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 12.dp)
                            )
                            Box(modifier = Modifier.weight(1f)) {
                                StatItem(
                                    dotColor = StatusDanger,
                                    value = String.format("%02d", animatedWrong),
                                    label = "Salah / Kosong"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Tombol Navigasi Bawah
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp)
                        .graphicsLayer {
                            alpha = actionsAlpha.value
                            translationY = actionsSlide.value.dp.toPx()
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    BottomActionButton(
                        icon = Icons.Default.Visibility,
                        label = "Review",
                        circleColor = Color(0xFFD97706).copy(alpha = 0.13f),
                        iconTint = Color(0xFFD97706),
                        borderColor = Color(0xFFD97706).copy(alpha = 0.25f),
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
                        circleColor = SageSoft,
                        iconTint = SageDark,
                        borderColor = SagePrimary.copy(alpha = 0.28f),
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
                .size(9.dp)
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
    borderColor: Color = BorderSubtle,
    isMain: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "actionScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = circleColor,
            shadowElevation = if (isMain) 6.dp else 2.dp,
            border = if (isMain) null else BorderStroke(1.dp, borderColor),
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
