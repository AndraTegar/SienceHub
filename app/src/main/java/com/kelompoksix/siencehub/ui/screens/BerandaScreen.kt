package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kelompoksix.siencehub.ui.components.HeaderAtas
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.kelompoksix.siencehub.ui.components.FloatingBottomNav
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.CardAksi
import com.kelompoksix.siencehub.ui.components.CardStatus
import com.kelompoksix.siencehub.ui.components.CardUtama
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

@Composable
fun KerangkaAplikasi() {

    var indexAktif by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        // ==================================================
        // LAYER 1
        // KONTEN HALAMAN
        // ==================================================

        when (indexAktif) {

            0 -> BerandaScreen(
                onNavigateToMateri = {
                    indexAktif = 1
                }
            )

            // Memanggil MateriScreen yang sebenarnya (bukan placeholder teks lagi)
            1 -> MateriScreen()

            2 -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ini Halaman Profil"
                )
            }
        }


        // ==================================================
        // LAYER 2
        // FLOATING BOTTOM NAVIGATION
        // ==================================================

        FloatingBottomNav(
            selectedIndex = indexAktif,
            onItemSelected = { index ->
                indexAktif = index
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 30.dp,
                    end = 30.dp,
                    bottom = 50.dp
                )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(onNavigateToMateri: () -> Unit) {
    val warnaHijauSage = Color(0xFF869E83)

    // Mesin untuk membaca gerakan jari (scroll naik/turun)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

    // DATA DUMMY UNTUK KARTU YANG BISA DI-SLIDE
    val daftarMateri = listOf(
        Pair("Biologi: Struktur Sel", 0.1f),
        Pair("Fisika: Hukum Newton", 0.5f),
        Pair("Kimia: Reaksi Asam Basa", 0.8f)
    )
    val pagerState = rememberPagerState(pageCount = { daftarMateri.size })

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    HeaderAtas(
                        onSearchClick = {}, onNotificationClick = {}, onProfileClick = {}
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = warnaHijauSage,
                    scrolledContainerColor = warnaHijauSage
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        // WADAH UTAMA: Memisahkan background dan konten scroll
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // BACKGROUND HIJAU STATIS: Hanya digambar di setengah layar atas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .background(warnaHijauSage)
            )

            // KONTEN UTAMA (Yang bisa di-scroll)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                // --- AREA HIJAU ---
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    CardStatus(level = 1, streak = 5)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Lanjutkan belajar",
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
                            onClick = { onNavigateToMateri() }
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

                // --- AREA PUTIH ---
                Surface(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Aktivitas Seru Hari Ini",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CardAksi(
                            judul = "Tantangan Harian \uD83D\uDD25",
                            deskripsi = "Selesaikan 5 soal kuis sistem pencernaan berturut-turut tanpa salah.",
                            teksTombol = "Mulai Kuis",
                            ikon = Icons.Default.PlayArrow,
                            warnaBackground = Color(0xFFE8ECE7),
                            warnaTombol = Color(0xFF869E83),
                            onClick = { /* Navigasi ke kuis */ }
                        )

                        CardAksi(
                            judul = "Eksperimen Virtual \uD83D\uDD2C",
                            deskripsi = "Simulasikan bagaimana hukum gravitasi bekerja di berbagai planet.",
                            teksTombol = "Mainkan",
                            ikon = Icons.Default.Science,
                            warnaBackground = Color(0xFFE8ECE7),
                            warnaTombol = Color(0xFF869E83),
                            onClick = { /* Navigasi ke simulasi */ }
                        )

                        CardAksi(
                            judul = "Fakta Menarik \uD83D\uDCA1",
                            deskripsi = "Kenapa langit berwarna biru? Temukan jawabannya dalam bacaan 2 menit ini.",
                            teksTombol = "Baca Artikel",
                            ikon = Icons.Default.Person,
                            warnaBackground = Color(0xFFE8ECE7),
                            warnaTombol = Color(0xFF869E83),
                            onClick = { /* Navigasi ke artikel */ }
                        )

                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen(
        onNavigateToMateri = {}
    )
}

@Preview(showBackground = true)
@Composable
fun KerangkaAplikasiPreview() {
    KerangkaAplikasi()
}