package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.ui.components.CardAksi
import com.kelompoksix.siencehub.ui.components.CardUtama
import com.kelompoksix.siencehub.ui.components.SearchBarMateri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    onNavigateToMateri: (String) -> Unit = {},
    onHasilCari: (TopikMateri, Int?) -> Unit = { _, _ -> }
) {
    val warnaHijauSage = Color(0xFF7A8B76) // Dibuat selaras dengan halaman profil
    val scrollState = rememberScrollState()

    val daftarMateri = listOf(
        Pair("Biologi: Struktur Sel", 0.1f),
        Pair("Fisika: Hukum Newton", 0.5f),
        Pair("Kimia: Reaksi Asam Basa", 0.8f)
    )
    val pagerState = rememberPagerState(pageCount = { daftarMateri.size })

    Scaffold(
        containerColor = Color.White,
        topBar = {
            // TOP BAR MODERN: Search Bar Full + Lonceng Notifikasi
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SearchBarMateri(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            onHasilClick = onHasilCari
                        )

                        // Tombol Ikon Lonceng Notifikasi
                        IconButton(
                            onClick = { /* Aksi notifikasi */ },
                            modifier = Modifier
                                .size(45.dp)
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = warnaHijauSage,
                    scrolledContainerColor = warnaHijauSage
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background Hijau Sage Setengah Layar Atas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .background(warnaHijauSage)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // ==========================================
                    // KARTU STATUS BARU (Level 1 & Streak 6)
                    // ==========================================
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Level Badge
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color(0xFFE1F5FE), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF00B0FF), modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "Level 1", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                                    Text(text = "Pemula", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }

                            // Garis Pemisah
                            Box(modifier = Modifier.height(30.dp).width(1.dp).background(Color.White.copy(alpha = 0.3f)))

                            // Streak Badge
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color(0xFFFFF3E0), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF6D00), modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "6 Hari", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                                    Text(text = "Streak Aktif", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Lanjutkan Belajar",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
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
                            .padding(top = 16.dp),
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
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Bagian Bawah: Konten Putih Melengkung (Aktivitas Seru)
                Surface(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Aktivitas Seru Hari Ini",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CardAksi(
                            judul = "Tantangan Harian \uD83D\uDD25",
                            deskripsi = "Selesaikan 5 soal kuis sistem pencernaan.",
                            teksTombol = "Mulai Kuis",
                            ikon = Icons.Default.PlayArrow,
                            warnaBackground = Color(0xFFE8ECE7),
                            warnaTombol = warnaHijauSage,
                            onClick = { }
                        )
                        CardAksi(
                            judul = "Eksperimen Virtual \uD83D\uDD2C",
                            deskripsi = "Simulasikan hukum gravitasi di berbagai planet.",
                            teksTombol = "Mainkan",
                            ikon = Icons.Default.Science,
                            warnaBackground = Color(0xFFE8ECE7),
                            warnaTombol = warnaHijauSage,
                            onClick = { }
                        )
                        CardAksi(
                            judul = "Fakta Menarik \uD83D\uDCA1",
                            deskripsi = "Kenapa langit berwarna biru? Temukan jawabannya.",
                            teksTombol = "Baca Artikel",
                            ikon = Icons.Default.Person,
                            warnaBackground = Color(0xFFE8ECE7),
                            warnaTombol = warnaHijauSage,
                            onClick = { }
                        )
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
    }
}