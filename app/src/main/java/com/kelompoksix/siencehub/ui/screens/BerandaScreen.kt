package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Search
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
import com.kelompoksix.siencehub.ui.components.CardAksi
import com.kelompoksix.siencehub.ui.components.CardUtama
import com.kelompoksix.siencehub.ui.theme.GreenBackground
import com.kelompoksix.siencehub.ui.theme.GreenLight
import com.kelompoksix.siencehub.ui.theme.Cream90
import com.kelompoksix.siencehub.ui.theme.CreamLight
import com.kelompoksix.siencehub.ui.theme.GrayColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(onNavigateToMateri: () -> Unit = {}) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

    val daftarMateri = listOf(
        Pair("Biologi: Struktur Sel", 0.1f),
        Pair("Fisika: Hukum Newton", 0.5f),
        Pair("Kimia: Reaksi Asam Basa", 0.8f)
    )
    val pagerState = rememberPagerState(pageCount = { daftarMateri.size })

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = GreenBackground, // Latar dasar disamakan dengan bagian atas
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Search Bar
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clickable { /* Aksi klik search bar */ },
                            shape = RoundedCornerShape(24.dp),
                            color = CreamLight.copy(alpha = 0.2f) // Menggunakan cream dengan transparansi
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Cari",
                                    tint = CreamLight,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Cari materi, kuis...",
                                    color = CreamLight.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Tombol Notifikasi
                        IconButton(
                            onClick = { /* Aksi notifikasi */ },
                            modifier = Modifier
                                .size(46.dp)
                                .background(CreamLight.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifikasi",
                                tint = CreamLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenBackground,
                    scrolledContainerColor = GreenBackground
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background Atas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .background(GreenBackground)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // KARTU STATUS (Level & Streak)
                // ==========================================
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CreamLight.copy(alpha = 0.15f)),
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
                        // Bagian Level (Kiri)
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Cream90, CircleShape), // Menggunakan Cream90
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GreenBackground, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Level 1", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CreamLight)
                                Text(text = "Pemula", fontSize = 12.sp, color = CreamLight.copy(alpha = 0.8f))
                            }
                        }

                        // Garis Pemisah (Tengah)
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .width(1.dp)
                                .background(CreamLight.copy(alpha = 0.3f))
                        )

                        // Bagian Streak (Kanan)
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Cream90, CircleShape), // Menggunakan Cream90
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = GreenBackground, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "6 Hari", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CreamLight)
                                Text(text = "Streak Aktif", fontSize = 12.sp, color = CreamLight.copy(alpha = 0.8f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Lanjutkan Belajar",
                    color = CreamLight,
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
                        onClick = { onNavigateToMateri() }
                    )
                }

                // Indikator Titik (Dots) Pager
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
                                // Titik aktif menggunakan CreamLight, tidak aktif GreenLight
                                .background(if (isSelected) CreamLight else GreenLight.copy(alpha = 0.5f))
                        )
                    }
                }

                // ==========================================
                // BAGIAN BAWAH (Latar Belakang CreamLight)
                // ==========================================
                Surface(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = CreamLight, // Warna background bawah lebih hangat, tidak putih pucat
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
                        Text(
                            text = "Aktivitas Seru Hari Ini",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrayColor // Teks abu-abu gelap agar nyaman dibaca
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        CardAksi(
                            judul = "Tantangan Harian \uD83D\uDD25",
                            deskripsi = "Selesaikan 5 soal kuis sistem pencernaan.",
                            teksTombol = "Mulai Kuis",
                            ikon = Icons.Default.PlayArrow,
                            warnaBackground = Color.White, // Putih agar menonjol dari latar CreamLight
                            warnaTombol = GreenBackground,
                            onClick = { }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        CardAksi(
                            judul = "Eksperimen Virtual \uD83D\uDD2C",
                            deskripsi = "Simulasikan hukum gravitasi di berbagai planet.",
                            teksTombol = "Mainkan",
                            ikon = Icons.Default.Science,
                            warnaBackground = Color.White,
                            warnaTombol = GreenBackground,
                            onClick = { }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        CardAksi(
                            judul = "Fakta Menarik \uD83D\uDCA1",
                            deskripsi = "Kenapa langit berwarna biru? Temukan jawabannya.",
                            teksTombol = "Baca Artikel",
                            ikon = Icons.Default.Person,
                            warnaBackground = Color.White,
                            warnaTombol = GreenBackground,
                            onClick = { }
                        )
                        Spacer(modifier = Modifier.height(90.dp))
                    }
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