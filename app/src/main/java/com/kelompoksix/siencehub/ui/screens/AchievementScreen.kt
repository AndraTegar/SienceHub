package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AchievementScreen(
    onBackClick: () -> Unit
) {
    val headerColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(headerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(50))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Kembali",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Pencapaian Kamu",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Terselesaikan \uD83C\uDF1F",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    KartuAchievement(
                        judul = "Langkah Pertama",
                        deskripsi = "Menyelesaikan 1 materi pertama di ScienceHub.",
                        ikon = Icons.Default.Star,
                        warnaIkon = Color(0xFFFFD54F),
                        warnaBgIkon = Color(0xFFFFF8E1),
                        sudahSelesai = true
                    )
                }

                item {
                    KartuAchievement(
                        judul = "Si Paling Rajin",
                        deskripsi = "Mencapai 5 hari streak belajar berturut-turut.",
                        ikon = Icons.Default.LocalFireDepartment,
                        warnaIkon = Color(0xFFFF8A65),
                        warnaBgIkon = Color(0xFFFBE9E7),
                        sudahSelesai = true
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)))
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "Belum Terbuka \uD83D\uDD12",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    KartuAchievement(
                        judul = "Ilmuwan Gila",
                        deskripsi = "Menyelesaikan kuis Biologi dan Kimia dengan nilai 100.",
                        ikon = Icons.Default.Science,
                        warnaIkon = Color.Gray,
                        warnaBgIkon = Color.LightGray,
                        sudahSelesai = false
                    )
                }

                item {
                    KartuAchievement(
                        judul = "Raja Kuis",
                        deskripsi = "Menjawab 50 soal kuis tanpa ada kesalahan satupun.",
                        ikon = Icons.Default.EmojiEvents,
                        warnaIkon = Color.Gray,
                        warnaBgIkon = Color.LightGray,
                        sudahSelesai = false
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun KartuAchievement(
    judul: String,
    deskripsi: String,
    ikon: androidx.compose.ui.graphics.vector.ImageVector,
    warnaIkon: Color,
    warnaBgIkon: Color,
    sudahSelesai: Boolean
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = if (sudahSelesai) 4.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (sudahSelesai) 1f else 0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(warnaBgIkon, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (sudahSelesai) ikon else Icons.Default.Lock,
                    contentDescription = null,
                    tint = warnaIkon,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = judul,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = deskripsi,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
