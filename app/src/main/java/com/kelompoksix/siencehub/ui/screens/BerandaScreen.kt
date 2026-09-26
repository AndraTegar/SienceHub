package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.kelompoksix.siencehub.ui.components.CardStatus
import com.kelompoksix.siencehub.ui.components.CardUtama

@Composable
fun KerangkaAplikasi() {

    var indexAktif by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier,

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

            1 -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ini Halaman Materi"
                )
            }

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // 1. UBAH INI: Pastikan dasar paling bawah layar berwarna putih
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

        // 2. WADAH UTAMA: Memisahkan background dan konten scroll
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // 3. BACKGROUND HIJAU STATIS: Hanya digambar di setengah layar atas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f) // Menutupi 60% area atas
                    .background(warnaHijauSage)
            )

            // 4. KONTEN UTAMA (Yang bisa di-scroll)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState) // Pakai scrollState yang sudah kamu buat
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

                    CardUtama(
                        judulMateri = "Biologi: Struktur Sel",
                        progress = 0.1f,
                        onClick = { }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }

                // --- AREA PUTIH ---
                val warnaBackgroundPutih = Color.White
                Surface(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = warnaBackgroundPutih,
                    modifier = Modifier.fillMaxWidth()
                    // Kamu sekarang bisa MENGHAPUS .defaultMinSize()
                    // karena latar belakang layar di bawahnya sudah putih!
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

                        // ... (Panggilan CardAksi ke-1, 2, 3 tetap sama) ...

                        // Spacer di paling bawah tetap butuh agar konten terakhir
                        // tidak tertutup oleh navigasi melayang saat di-scroll mentok
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
        onNavigateToMateri = TODO()
    )
}

@Preview(showBackground = true)
@Composable
fun KerangkaAplikasiPreview() {
    KerangkaAplikasi()
}
