package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.ui.components.CardAksi
import com.kelompoksix.siencehub.ui.components.CardUtama
import com.kelompoksix.siencehub.ui.components.SearchBarMateri
import com.kelompoksix.siencehub.ui.viewmodels.BerandaViewModel

@Composable
fun BerandaScreen(
    onNavigateToMateri: (String) -> Unit = {},
    onHasilCari: (TopikMateri, Int?) -> Unit = { _, _ -> },
    viewModel: BerandaViewModel = viewModel()
) {
    val warnaHijauSage = Color(0xFF7A8B76)
    val scrollState = rememberScrollState()

    val faktaSains = viewModel.faktaSains
    val isLoadingFakta = viewModel.isLoadingFakta

    val daftarMateri = listOf(
        Pair("Biologi: Struktur Sel", 0.1f),
        Pair("Fisika: Hukum Newton", 0.5f),
        Pair("Kimia: Reaksi Asam Basa", 0.8f)
    )
    val pagerState = rememberPagerState(pageCount = { daftarMateri.size })

    // 1. Root Box diubah background-nya jadi Putih agar area bawah aman dan menyatu
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 2. Tambahkan background Hijau statis di bagian atas layar saja (berada di belakang konten)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f) // Hijau menutupi 65% layar bagian atas
                .background(warnaHijauSage)
        )

        // 3. Kolom Konten yang bisa di-scroll (berada di atas background hijau & putih)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .systemBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SearchBarMateri(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    onHasilClick = { topik, babId ->
                        onHasilCari(topik, babId)
                    }
                )

                IconButton(
                    onClick = { /* Aksi notifikasi */ },
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color.White.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifikasi",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Level 1", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                            Text(text = "Pemula", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(Color.White.copy(alpha = 0.3f))
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF6D00), modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "6 Hari", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                            Text(text = "Streak Aktif", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Lanjutkan Belajar",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                pageSpacing = 16.dp
            ) { page ->
                val materi = daftarMateri[page]
                CardUtama(
                    judulMateri = materi.first,
                    progress = materi.second,
                    onClick = { onNavigateToMateri(materi.first) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(daftarMateri.size) { iteration ->
                    val isSelected = pagerState.currentPage == iteration
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.4f))
                    )
                }
            }

            // 4. Modifier `.weight(1f)` DIHAPUS dari Surface agar kolom bisa mengukur tinggi dan di-scroll
            Surface(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
                    Text(
                        text = "Aktivitas Seru Hari Ini",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    CardAksi(
                        judul = "Tantangan Harian \uD83D\uDD25",
                        deskripsi = "Selesaikan 5 soal kuis sistem pencernaan.",
                        teksTombol = "Mulai Kuis",
                        ikon = Icons.Default.PlayArrow,
                        warnaBackground = Color(0xFFE8ECE7),
                        warnaTombol = warnaHijauSage,
                        onClick = { }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CardAksi(
                        judul = "Eksperimen Virtual \uD83D\uDD2C",
                        deskripsi = "Simulasikan hukum gravitasi di berbagai planet.",
                        teksTombol = "Mainkan",
                        ikon = Icons.Default.Science,
                        warnaBackground = Color(0xFFE8ECE7),
                        warnaTombol = warnaHijauSage,
                        onClick = { }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CardAksi(
                        judul = "Fakta Menarik \uD83D\uDCA1",
                        deskripsi = if (isLoadingFakta) "Memuat Fakta Menarik" else faktaSains,
                        teksTombol = if (isLoadingFakta) "Memuat..." else "Fakta Baru \uD83D\uDCA1",
                        ikon = Icons.Default.Person,
                        warnaBackground = Color(0xFFE8ECE7),
                        warnaTombol = warnaHijauSage,
                        onClick = { viewModel.muatFaktaBaru() }
                    )

                    // Spacer bawah ini akan menjaga agar konten tidak tertutup Floating Navbar
                    Spacer(modifier = Modifier.height(140.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen()
}