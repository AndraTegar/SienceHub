package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.repositories.LeaderboardRepository
import com.kelompoksix.siencehub.data.repositories.QuizRepository
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    subjectName: String,
    onBackClick: () -> Unit = {},
    onQuizFinished: (score: Int, total: Int, correct: Int, wrong: Int) -> Unit
) {
    val daftarSoal = remember { QuizRepository.getSoalBySubject(subjectName) }
    var indexSoalAktif by remember { mutableIntStateOf(0) }
    val jawabanUser = remember { mutableStateMapOf<Int, Int>() }

    // Total waktu per kuis/soal
    val totalWaktuDetik = 60
    var sisaWaktuDetik by remember { mutableIntStateOf(totalWaktuDetik) }

    // Warna Palet Presisi Figma
    val sageGreen = Color(0xFF859585)
    val purplePrimary = Color(0xFF8B4CFC)
    val purpleBorder = Color(0xFFA855F7)
    val sheetBackground = Color(0xFFFAF9FF)

    val correctCount = jawabanUser.entries.count { (idx, ops) -> daftarSoal.getOrNull(idx)?.jawabanBenar == ops }
    val wrongCount = jawabanUser.entries.count { (idx, ops) -> daftarSoal.getOrNull(idx)?.jawabanBenar != ops }

    // Timer Countdown
    LaunchedEffect(sisaWaktuDetik) {
        if (sisaWaktuDetik > 0) {
            delay(1000L)
            sisaWaktuDetik--
        } else {
            val totalSkor = correctCount * 20
            LeaderboardRepository.tambahSkorUser(totalSkor)
            onQuizFinished(totalSkor, daftarSoal.size * 20, correctCount, wrongCount)
        }
    }

    val soalSaatIni = daftarSoal.getOrNull(indexSoalAktif)

    if (soalSaatIni == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Soal kuis belum tersedia.")
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(sageGreen)
    ) {
        // 1. Latar Belakang Putih Bagian Bawah dengan Sudut Atas Melengkung
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.68f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = sheetBackground
        ) {}

        // 2. Konten Utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // TOP BAR (Tombol Kembali & Breadcrumb)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBackClick() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Cek Kemampuan > $subjectName",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // KARTU SOAL (Melayang di atas perbatasan warna)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 36.dp, bottom = 28.dp)
                    ) {
                        // Indikator Benar/Salah (Green & Orange Bars)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("$correctCount", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF16A34A))
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF16A34A))
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEA580C))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("$wrongCount", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFEA580C))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Question ${indexSoalAktif + 1}/${daftarSoal.size}",
                            color = purplePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = soalSaatIni.pertanyaan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }

                // TIMER CIRCLE DENGAN ANIMASI ARC BERKURANG
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

                            // Lingkaran Latar Belakang (Track Ungu Muda)
                            drawCircle(
                                color = Color(0xFFF3E8FF),
                                style = Stroke(width = strokeWidth)
                            )

                            // Busur Waktu (Menipis/Berkurang Searah Jarum Jam)
                            val sweepAngle = 360f * (sisaWaktuDetik.toFloat() / totalWaktuDetik.toFloat())
                            drawArc(
                                color = purplePrimary,
                                startAngle = -90f,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Text(
                            text = "$sisaWaktuDetik",
                            color = purplePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DAFTAR OPSI JAWABAN
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                soalSaatIni.opsi.forEachIndexed { indexOpsi, teksOpsi ->
                    val isSelected = jawabanUser[indexSoalAktif] == indexOpsi

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable {
                                jawabanUser[indexSoalAktif] = indexOpsi

                                if (indexSoalAktif < daftarSoal.size - 1) {
                                    indexSoalAktif++
                                } else {
                                    val finalCorrect = jawabanUser.entries.count { (idx, ops) -> daftarSoal.getOrNull(idx)?.jawabanBenar == ops }
                                    val finalWrong = daftarSoal.size - finalCorrect
                                    val totalSkor = finalCorrect * 20
                                    LeaderboardRepository.tambahSkorUser(totalSkor)
                                    onQuizFinished(totalSkor, daftarSoal.size * 20, finalCorrect, finalWrong)
                                }
                            },
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) Color(0xFFF3E8FF) else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.5.dp,
                            color = if (isSelected) purplePrimary else purpleBorder
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = teksOpsi,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF334155),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}