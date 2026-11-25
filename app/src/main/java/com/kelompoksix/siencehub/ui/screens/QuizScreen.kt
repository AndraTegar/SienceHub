package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.repositories.LeaderboardRepository
import com.kelompoksix.siencehub.data.repositories.LocalLearningRepository
import com.kelompoksix.siencehub.data.repositories.QuizHistoryRepository
import com.kelompoksix.siencehub.data.repositories.QuizRepository
import com.kelompoksix.siencehub.ui.theme.AccentPurple
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.StatusSuccess
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    subjectName: String,
    onBackClick: () -> Unit = {},
    onQuizFinished: (score: Int, total: Int, correct: Int, wrong: Int) -> Unit
) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()
    val daftarSoal = remember { QuizRepository.getSoalBySubject(subjectName) }
    var indexSoalAktif by remember { mutableIntStateOf(0) }
    val jawabanUser = remember { mutableStateMapOf<Int, Int>() }
    var quizFinished by remember { mutableStateOf(false) }

    val totalWaktuDetik = 60
    var sisaWaktuDetik by remember { mutableIntStateOf(totalWaktuDetik) }

    val purpleBorder = Color(0xFFD8B4FE)
    val sheetBackground = Color(0xFFFAF9FF)

    val correctCount = jawabanUser.entries.count { (idx, ops) -> daftarSoal.getOrNull(idx)?.jawabanBenar == ops }
    val wrongCount = jawabanUser.entries.count { (idx, ops) -> daftarSoal.getOrNull(idx)?.jawabanBenar != ops }

    LaunchedEffect(sisaWaktuDetik) {
        if (sisaWaktuDetik > 0) {
            delay(1000L)
            sisaWaktuDetik--
        } else if (!quizFinished) {
            quizFinished = true
            val totalSkor = correctCount * 20
            LeaderboardRepository.tambahSkorUser(totalSkor)
            QuizHistoryRepository.simpanHasilKuis(
                context = context,
                subjectName = subjectName,
                score = totalSkor,
                totalQuestions = daftarSoal.size,
                correctCount = correctCount,
                wrongCount = wrongCount
            )
            LocalLearningRepository.recordQuizResult(
                context = context,
                subjectName = subjectName,
                score = totalSkor,
                totalScore = daftarSoal.size * 20,
                correctAnswers = correctCount
            )
            onQuizFinished(totalSkor, daftarSoal.size * 20, correctCount, wrongCount)
        }
    }

    val soalSaatIni = daftarSoal.getOrNull(indexSoalAktif)

    if (soalSaatIni == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceOffWhite)
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text("Soal kuis belum tersedia.", color = TextPrimary)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.68f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = sheetBackground
        ) {}

        ResponsiveContentContainer(maxWidth = 640.dp) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = metrics.horizontalPadding, vertical = 12.dp)
            ) {
                // Top Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        onClick = onBackClick,
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Cek Kemampuan • $subjectName",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Kartu Soal dengan Timer Lingkaran
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 36.dp, bottom = 26.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("$correctCount", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StatusSuccess)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(6.dp)
                                            .clip(CircleShape)
                                            .background(StatusSuccess)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(6.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEA580C))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("$wrongCount", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFEA580C))
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Question ${indexSoalAktif + 1}/${daftarSoal.size}",
                                color = AccentPurple,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = soalSaatIni.pertanyaan,
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center,
                                lineHeight = 23.sp,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize().padding(5.dp)) {
                                val strokeWidth = 3.5.dp.toPx()
                                drawCircle(
                                    color = Color(0xFFF3E8FF),
                                    style = Stroke(width = strokeWidth)
                                )
                                val sweepAngle = 360f * (sisaWaktuDetik.toFloat() / totalWaktuDetik.toFloat())
                                drawArc(
                                    color = AccentPurple,
                                    startAngle = -90f,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            Text(
                                text = "$sisaWaktuDetik",
                                color = AccentPurple,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Opsi Jawaban Responsif (heightIn agar teks panjang tidak terpotong)
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    soalSaatIni.opsi.forEachIndexed { indexOpsi, teksOpsi ->
                        val isSelected = jawabanUser[indexSoalAktif] == indexOpsi

                        Surface(
                            onClick = {
                                if (!quizFinished) {
                                    jawabanUser[indexSoalAktif] = indexOpsi

                                    if (indexSoalAktif < daftarSoal.size - 1) {
                                        indexSoalAktif++
                                    } else {
                                        val finalCorrect = jawabanUser.entries.count { (idx, ops) ->
                                            daftarSoal.getOrNull(idx)?.jawabanBenar == ops
                                        }
                                        val finalWrong = daftarSoal.size - finalCorrect
                                        val totalSkor = finalCorrect * 20
                                        quizFinished = true
                                        LeaderboardRepository.tambahSkorUser(totalSkor)
                                        QuizHistoryRepository.simpanHasilKuis(
                                            context = context,
                                            subjectName = subjectName,
                                            score = totalSkor,
                                            totalQuestions = daftarSoal.size,
                                            correctCount = finalCorrect,
                                            wrongCount = finalWrong
                                        )
                                        LocalLearningRepository.recordQuizResult(
                                            context = context,
                                            subjectName = subjectName,
                                            score = totalSkor,
                                            totalScore = daftarSoal.size * 20,
                                            correctAnswers = finalCorrect
                                        )
                                        onQuizFinished(totalSkor, daftarSoal.size * 20, finalCorrect, finalWrong)
                                    }
                                }
                            },
                            enabled = !quizFinished,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = if (isSelected) Color(0xFFF3E8FF) else Color.White,
                            border = BorderStroke(
                                width = 1.5.dp,
                                color = if (isSelected) AccentPurple else purpleBorder
                            ),
                            shadowElevation = 1.dp
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 14.dp)
                            ) {
                                Text(
                                    text = teksOpsi,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
