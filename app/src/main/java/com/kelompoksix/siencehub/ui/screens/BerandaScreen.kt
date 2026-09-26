package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kelompoksix.siencehub.ui.components.HeaderAtas
import com.kelompoksix.siencehub.ui.components.CardStatus
import com.kelompoksix.siencehub.ui.components.CardUtama
import com.kelompoksix.siencehub.ui.components.CardAksi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.kelompoksix.siencehub.ui.components.FloatingBottomNav
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll

@Composable
fun KerangkaAplikasi() {
    var indexAktif by remember { mutableIntStateOf(0) }

    // Box adalah tumpukan layer Z-Index
    Box(modifier = Modifier.fillMaxSize()) {

        // LAYER BAWAH: Konten Halaman
        when (indexAktif) {
            0 -> BerandaScreen()
            1 -> Text("Ini Halaman Materi", modifier = Modifier.fillMaxSize().background(Color.White))
            2 -> Text("Ini Halaman Profil", modifier = Modifier.fillMaxSize().background(Color.White))
        }

        // LAYER ATAS: Navigasi Melayang (Dipaksa ke posisi Bawah-Tengah)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            FloatingBottomNav(
                selectedIndex = indexAktif,
                onItemSelected = { indexAktif = it }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    onNavigateToMateri: () -> Unit = {}
) {
    val warnaHijauSage = Color(0xFF869E83)
    val warnaBackgroundPutih = Color(0xFFFAFAFF)

    // Mesin untuk melacak gerakan scroll ke atas/bawah
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

    Scaffold(
        // Menghubungkan gerakan scroll layar dengan header
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = warnaHijauSage,
        topBar = {
            // Header yang bisa sembunyi/muncul otomatis
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ==========================================
            // LAYER 1 (BELAKANG): Statis di area hijau
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // 1. Header (Avatar & Search)
                HeaderAtas(
                    onSearchClick = {
                        // Nanti kita isi logika untuk pindah ke halaman pencarian
                        println("Tombol Search diklik")
                    },
                    onNotificationClick = {
                        // Nanti kita isi logika untuk membuka panel notifikasi
                        println("Tombol Notifikasi diklik")
                    },
                    onProfileClick = {
                        // Nanti kita isi logika untuk pindah ke tab profil
                        println("Avatar Profil diklik")
                    }
                )

                Spacer(modifier = Modifier.height(2.dp))

                // 2. Card Status (Level & Streak)
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                // MANGGIL KOMPONEN CARD STATUS DI SINI
                CardStatus(level = 1, streak = 5) // Kita isi angka dummy 1 dan 5 dulu

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Lanjutkan belajar",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Card Utama (Beige) dipindah ke sini!
                CardUtama(
                    judulMateri = "Biologi: Struktur Sel",
                    progress = 0.1f, // Artinya 60%
                    onClick = {
                        println("Lanjut belajar diklik")
                        onNavigateToMateri()
                    }
                )
            }

            // ==========================================
            // LAYER 2 (DEPAN): Area putih dinamis (Scrollable)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Spacer diperbesar tingginya agar Layer 1 (termasuk Card Beige)
                // terlihat semua sebelum area putih mulai.
                Spacer(modifier = Modifier.height(480.dp))

                // Kanvas Putih Melengkung
                Surface(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = warnaBackgroundPutih,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 800.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp) // Beri padding sedikit melebih lebar agar lega
                    ) {

                        // Judul Section
                        Text(
                            text = "Aktivitas Seru Hari Ini",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // MANGGIL KOMPONEN REUSABLE (1)
                        CardAksi(
                            judul = "Tantangan Harian \uD83D\uDD25",
                            deskripsi = "Selesaikan 5 soal kuis sistem pencernaan berturut-turut tanpa salah.",
                            teksTombol = "Mulai Kuis",
                            ikon = Icons.Default.PlayArrow,
                            warnaBackground = Color(0xFF859D82),
                            warnaTombol = Color(0xFF673AB7),
                            onClick = { /* Navigasi ke kuis */ }
                        )

                        // MANGGIL KOMPONEN REUSABLE (2)
                        CardAksi(
                            judul = "Eksperimen Virtual \uD83D\uDD2C",
                            deskripsi = "Simulasikan bagaimana hukum gravitasi bekerja di berbagai planet.",
                            teksTombol = "Mainkan",
                            ikon = Icons.Default.Science,
                            warnaBackground = Color(0xFFE8F5E9),
                            warnaTombol = Color(0xFF4CAF50),
                            onClick = { /* Navigasi ke game/simulasi */ }
                        )

                        // MANGGIL KOMPONEN REUSABLE (3)
                        CardAksi(
                            judul = "Fakta Menarik \uD83D\uDCA1",
                            deskripsi = "Kenapa langit berwarna biru? Temukan jawabannya dalam bacaan 2 menit ini.",
                            teksTombol = "Baca Artikel",
                            ikon = Icons.Default.Person, // Nanti bisa diganti ikon buku
                            warnaBackground = Color(0xFFE3F2FD),
                            warnaTombol = Color(0xFF2196F3),
                            onClick = { /* Navigasi ke artikel */ }
                        )

                        Spacer(modifier = Modifier.height(100.dp)) // Ruang ekstra di bawah untuk area navigasi
                    }
                }
            }
        }
    }
}
