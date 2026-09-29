package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.WavyShape
import com.kelompoksix.siencehub.R

@Composable
fun WelcomeScreen(onContinueClick: () -> Unit = {}) {
    // 1. Buat daftar kalimat yang akan diacak
    val welcomeMessages = listOf(
        "Jelajahi keajaiban sains dan raih pengetahuan baru setiap hari.",
        "Sains adalah kunci utama untuk membuka rahasia alam semesta.",
        "Temukan berbagai wawasan, fakta, dan eksperimen menarik di sini.",
        "Mulailah petualangan ilmiahmu bersama komunitas ScienceHub."
    )

    // 2. Pilih satu kalimat secara acak saat layar dimuat pertama kali
    val randomMessage = remember { welcomeMessages.random() }

    // Box utama untuk menumpuk background dan konten
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FC)) // Warna putih sedikit abu-abu
    ) {

        // Gambar Latar Belakang Hijau
        Image(
            painter = painterResource(id = R.drawable.texture_bg),
            contentDescription = "Background Topography",
            contentScale = ContentScale.Crop, // Ubah ke FillBounds jika gambar kurang lebar
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f) // Mengambil 60% tinggi layar
                .clip(WavyShape()) // Memotong gambar dengan bentuk gelombang
        )

        // Konten Teks dan Tombol
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 43.dp),
            verticalArrangement = Arrangement.Bottom // Mendorong konten ke bawah
        ) {

            Text(
                text = "Welcome",
                color = Color(0xFF333333),
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 3. Tampilkan kalimat acak yang sudah disimpan di variabel randomMessage
            Text(
                text = randomMessage,
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 64.dp)
            )

            // Tombol Continue di bagian kanan bawah
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = Color(0xFF7A8B76), shape = CircleShape) // Warna hijau tombol
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                        contentDescription = "Continue",
                        tint = Color.White
                    )
                }
            }
        }
    }
}