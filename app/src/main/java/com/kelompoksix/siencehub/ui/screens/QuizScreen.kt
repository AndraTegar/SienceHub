package com.kelompoksix.siencehub.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
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
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.data.repositories.QuizHistoryRepository
import com.kelompoksix.siencehub.data.repositories.QuizRepository
import com.kelompoksix.siencehub.data.repositories.QuizSessionResult
import com.kelompoksix.siencehub.ui.theme.AccentPurple
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.StatusDanger
import com.kelompoksix.siencehub.ui.theme.StatusSuccess
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import com.kelompoksix.siencehub.utils.AppSettingsManager
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    subjectName: String,
    onBackClick: () -> Unit = {},
    onQuizFinished: (score: Int, total: Int, correct: Int, wrong: Int) -> Unit
) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()
    val displaySubject = remember(subjectName) { MateriRepository.normalizeSubjectName(subjectName) }

    // Soal & posisi opsi diacak setiap sesi baru dimulai
    val daftarSoal = remember(subjectName) { QuizRepository.prepareShuffledQuiz(subjectName) }
    var indexSoalAktif by remember { mutableIntStateOf(0) }
    val jawabanUser = remember { mutableStateMapOf<Int, Int>() }
    var quizFinished by remember { mutableStateOf(false) }

    var showSubmitConfirmDialog by remember { mutableStateOf(false) }
    var showExitConfirmDialog by remember { mutableStateOf(false) }

    val totalWaktuDetik = 120
    var sisaWaktuDetik by remember { mutableIntStateOf(totalWaktuDetik) }

    val purpleBorder = if (AppSettingsManager.isDarkMode) AccentPurple.copy(alpha = 0.45f) else Color(0xFFD8B4FE)
    val sheetBackground = SurfaceOffWhite
    val numberRowState = rememberLazyListState()

    val submitQuiz: () -> Unit = {
        if (!quizFinished) {
            quizFinished = true
            val snapshotAnswers = jawabanUser.toMap()
            val finalCorrect = daftarSoal.indices.count { idx ->
                val userChoice = snapshotAnswers[idx]
                userChoice != null && daftarSoal[idx].jawabanBenar == userChoice
            }
            val wrongAnswered = daftarSoal.indices.count { idx ->
                val userChoice = snapshotAnswers[idx]
                userChoice != null && daftarSoal[idx].jawabanBenar != userChoice
            }
            val unanswered = (daftarSoal.size - snapshotAnswers.size).coerceAtLeast(0)
            val totalWrongOrEmpty = daftarSoal.size - finalCorrect
            val totalSkor = finalCorrect * 20

            QuizRepository.saveSessionResult(
                QuizSessionResult(
                    subjectName = displaySubject,
                    questions = daftarSoal,
                    userAnswers = snapshotAnswers,
                    score = totalSkor,
                    correctCount = finalCorrect,
                    wrongAnsweredCount = wrongAnswered,
                    unansweredCount = unanswered
                )
            )

            LeaderboardRepository.tambahSkorUser(totalSkor)
            QuizHistoryRepository.simpanHasilKuis(
                context = context,
                subjectName = displaySubject,
                score = totalSkor,
                totalQuestions = daftarSoal.size,
                correctCount = finalCorrect,
                wrongCount = totalWrongOrEmpty
            )
            LocalLearningRepository.recordQuizResult(
                context = context,
                subjectName = displaySubject,
                score = totalSkor,
                totalScore = daftarSoal.size * 20,
                correctAnswers = finalCorrect
            )
            onQuizFinished(totalSkor, daftarSoal.size * 20, finalCorrect, totalWrongOrEmpty)
        }
    }

    LaunchedEffect(indexSoalAktif) {
        numberRowState.animateScrollToItem(indexSoalAktif)
    }

    LaunchedEffect(sisaWaktuDetik, quizFinished) {
        if (quizFinished) return@LaunchedEffect
        if (sisaWaktuDetik > 0) {
            delay(1000L)
            sisaWaktuDetik--
        } else {
            showSubmitConfirmDialog = false
            submitQuiz()
        }
    }

    val handleExitAttempt: () -> Unit = {
        if (jawabanUser.isNotEmpty() && !quizFinished) {
            showExitConfirmDialog = true
        } else {
            onBackClick()
        }
    }

    BackHandler(enabled = !quizFinished) {
        handleExitAttempt()
    }

    if (showExitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showExitConfirmDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Keluar dari Kuis?",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Kamu sudah menjawab ${jawabanUser.size} dari ${daftarSoal.size} soal. Jika keluar sekarang, jawaban kuis ini tidak akan disimpan.",
                    color = TextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirmDialog = false
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Keluar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmDialog = false }) {
                    Text("Lanjut Kuis", color = SagePrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showSubmitConfirmDialog) {
        val belumTerjawab = (daftarSoal.size - jawabanUser.size).coerceAtLeast(0)
        AlertDialog(
            onDismissRequest = { showSubmitConfirmDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = if (belumTerjawab > 0) "Masih Ada Soal Kosong" else "Kumpulkan Kuis Sekarang?",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = if (belumTerjawab > 0) {
                        "Masih ada $belumTerjawab soal dari ${daftarSoal.size} soal yang belum kamu jawab. Soal kosong akan dihitung salah. Yakin ingin mengumpulkan?"
                    } else {
                        "Seluruh ${daftarSoal.size} soal sudah terjawab. Pastikan jawabanmu sudah yakin sebelum dikumpulkan."
                    },
                    color = TextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmDialog = false
                        submitQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Kumpulkan", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmDialog = false }) {
                    Text("Periksa Lagi", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
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

    val isLowTime = sisaWaktuDetik <= 20
    val timerColor = if (isLowTime) StatusDanger else AccentPurple
    val formattedTimer = remember(sisaWaktuDetik) {
        val m = sisaWaktuDetik / 60
        val s = sisaWaktuDetik % 60
        if (m > 0) "%d:%02d".format(m, s) else "${s}s"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.70f)
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
                // Top Bar + Tombol Kumpulkan Cepat
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        onClick = handleExitAttempt,
                        shape = CircleShape,
                        color = SurfaceWhite.copy(alpha = 0.94f),
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

                    Spacer(modifier = Modifier.width(10.dp))

                    Surface(
                        shape = CircleShape,
                        color = SurfaceWhite.copy(alpha = 0.94f),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Kuis • $displaySubject",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${jawabanUser.size}/${daftarSoal.size} dijawab",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SagePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Navigasi Nomor Soal (Bisa Loncat ke Soal Mana Saja)
                LazyRow(
                    state = numberRowState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(daftarSoal) { idx, _ ->
                        val isCurrent = idx == indexSoalAktif
                        val isAnswered = jawabanUser.containsKey(idx)
                        val pillBg = when {
                            isCurrent -> AccentPurple
                            isAnswered -> SurfaceWhite
                            else -> Color.White.copy(alpha = 0.22f)
                        }
                        val pillText = when {
                            isCurrent -> Color.White
                            isAnswered -> AccentPurple
                            else -> Color.White
                        }
                        val pillBorder = when {
                            isCurrent -> Color.White
                            isAnswered -> AccentPurple
                            else -> Color.White.copy(alpha = 0.35f)
                        }

                        Surface(
                            onClick = { indexSoalAktif = idx },
                            shape = CircleShape,
                            color = pillBg,
                            border = BorderStroke(1.5.dp, pillBorder),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = pillText
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Kartu Soal dengan Timer Lingkaran
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 36.dp, bottom = 24.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { jawabanUser.size.toFloat() / daftarSoal.size.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = StatusSuccess,
                                trackColor = BorderSubtle,
                                strokeCap = StrokeCap.Round
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Pertanyaan ${indexSoalAktif + 1} dari ${daftarSoal.size}",
                                color = AccentPurple,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

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
                        color = SurfaceWhite,
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(58.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize().padding(5.dp)) {
                                val strokeWidth = 3.5.dp.toPx()
                                drawCircle(
                                    color = timerColor.copy(alpha = 0.18f),
                                    style = Stroke(width = strokeWidth)
                                )
                                val sweepAngle = 360f * (sisaWaktuDetik.toFloat() / totalWaktuDetik.toFloat())
                                drawArc(
                                    color = timerColor,
                                    startAngle = -90f,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            Text(
                                text = formattedTimer,
                                color = timerColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Opsi Jawaban A / B / C / D (Bisa diubah sebelum dikumpulkan!)
                Column(
                    verticalArrangement = Arrangement.spacedBy(11.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    soalSaatIni.opsi.forEachIndexed { indexOpsi, teksOpsi ->
                        val isSelected = jawabanUser[indexSoalAktif] == indexOpsi
                        val optionLetter = ('A' + indexOpsi).toString()

                        Surface(
                            onClick = {
                                if (!quizFinished) {
                                    jawabanUser[indexSoalAktif] = indexOpsi
                                }
                            },
                            enabled = !quizFinished,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = if (isSelected) AccentPurple.copy(alpha = 0.15f) else SurfaceWhite,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.2.dp,
                                color = if (isSelected) AccentPurple else purpleBorder
                            ),
                            shadowElevation = if (isSelected) 2.dp else 1.dp
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 13.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            if (isSelected) AccentPurple else AccentPurple.copy(alpha = 0.12f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = optionLetter,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else AccentPurple
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Text(
                                    text = teksOpsi,
                                    fontSize = 14.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = TextPrimary,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Terpilih",
                                        tint = AccentPurple,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Baris Navigasi Sebelumnya / Selanjutnya / Kumpulkan Kuis
                val isFirstQuestion = indexSoalAktif == 0
                val isLastQuestion = indexSoalAktif == daftarSoal.size - 1

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { if (!isFirstQuestion) indexSoalAktif-- },
                        enabled = !isFirstQuestion && !quizFinished,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            1.dp,
                            if (!isFirstQuestion) BorderSubtle else BorderSubtle.copy(alpha = 0.4f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceWhite,
                            contentColor = TextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sebelumnya", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }

                    if (!isLastQuestion) {
                        Button(
                            onClick = { indexSoalAktif++ },
                            enabled = !quizFinished,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text("Selanjutnya", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = { showSubmitConfirmDialog = true },
                            enabled = !quizFinished,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Kumpulkan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }

                // Jika semua soal sudah dijawab tapi belum berada di soal terakhir, tampilkan pintasan Kumpulkan
                if (!isLastQuestion && jawabanUser.size == daftarSoal.size) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { showSubmitConfirmDialog = true },
                        enabled = !quizFinished,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Semua Terjawab • Kumpulkan Kuis Sekarang",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
