package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    var sisaWaktuDetik by remember { mutableIntStateOf(60) } // Timer detik per soal/kuis

    // Warna dari Figma
    val sageGreen = Color(0xFF859585)
    val purplePrimary = Color(0xFF8B4CFC)
    val purpleBorder = Color(0xFFA855F7)

    // Hitung jawaban benar & salah saat ini
    val correctCount = jawabanUser.entries.count { (idx, ops) -> daftarSoal.getOrNull(idx)?.jawabanBenar == ops }
    val wrongCount = jawabanUser.entries.count { (idx, ops) -> daftarSoal.getOrNull(idx)?.jawabanBenar != ops }

    // Logic Timer
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
            .background(Color(0xFFF8FAFC))
    ) {
        // Background Atas (Sage Green)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(sageGreen)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header: Tombol Back & Breadcrumb
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

            Spacer(modifier = Modifier.height(24.dp))

            // Kartu Soal Melayang
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
                        .padding(top = 24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 24.dp)
                    ) {
                        // Top Stats Bar (Green/Orange Indicator)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("$correctCount", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF16A34A))
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(modifier = Modifier.width(30.dp).height(6.dp).clip(CircleShape).background(Color(0xFF16A34A)))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.width(30.dp).height(6.dp).clip(CircleShape).background(Color(0xFFEA580C)))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("$wrongCount", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFEA580C))
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
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }

                // Timer Circle Badge (Posisi Melayang di Atas Card)
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .border(2.dp, purplePrimary, CircleShape)
                    ) {
                        Text(
                            text = "$sisaWaktuDetik",
                            color = purplePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Daftar Opsi Jawaban
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                soalSaatIni.opsi.forEachIndexed { indexOpsi, teksOpsi ->
                    val isSelected = jawabanUser[indexSoalAktif] == indexOpsi

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clickable {
                                jawabanUser[indexSoalAktif] = indexOpsi

                                // Pindah ke soal berikutnya secara otomatis atau selesaikan kuis
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
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) Color(0xFFF3E8FF) else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.5.dp,
                            color = if (isSelected) purplePrimary else purpleBorder
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
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