package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun WelcomeScreen() {
    // Box utama untuk menumpuk background dan konten
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FC)) // Warna putih sedikit abu-abu sesuai gambar
    ) {

        // 2. Gambar Latar Belakang Hijau
        Image(
            painter = painterResource(id = R.drawable.texture_bg), // Ganti dengan ID gambar Anda
            contentDescription = "Background Topography",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f) // Mengambil 65% tinggi layar
                .clip(WavyShape()) // Memotong gambar dengan bentuk gelombang
        )

        // 3. Konten Teks dan Tombol
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

            Text(
                text = "Ayo belajar di ScienceHub. Buat\nbelajarmu menjadi menyenangkan.",
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 64.dp)
            )

            // 4. Tombol Continue di bagian kanan bawah
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { /* Tambahkan aksi navigasi di sini */ },
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = Color(0xFF7A8B76), shape = CircleShape) // Warna hijau tombol
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward), // Ganti dengan ID ikon Anda
                        contentDescription = "Continue",
                        tint = Color.White
                    )
                }
            }
        }
    }
}