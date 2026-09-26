package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardAksi(
    judul: String,
    deskripsi: String,
    teksTombol: String,
    ikon: ImageVector, // Ikon dinamis di sebelah kanan
    warnaBackground: Color = Color(0xFFF3F4F6), // Default abu-abu terang
    warnaTombol: Color = Color(0xFF869E83), // Default hijau sage
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = warnaBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Tampilan flat modern
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp) // Jarak dengan card di bawahnya
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ==========================================
            // BAGIAN KIRI: Teks dan Tombol
            // ==========================================
            Column(
                modifier = Modifier.weight(1f) // Memakan sisa ruang agar ikon terdorong ke kanan
            ) {
                Text(
                    text = judul,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = deskripsi,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Aksi
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = warnaTombol),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = teksTombol,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // ==========================================
            // BAGIAN KANAN: Ikon/Ilustrasi
            // ==========================================
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.5f), // Efek transparan
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = ikon,
                    contentDescription = null,
                    tint = warnaTombol, // Warna ikon menyesuaikan warna tombol
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardAksiPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        // Contoh Penggunaan 1: Misi Kuis
        CardAksi(
            judul = "Tantangan Harian \uD83D\uDD25",
            deskripsi = "Selesaikan 5 soal kuis biologi berturut-turut tanpa salah untuk mendapat bonus +50 XP.",
            teksTombol = "Mulai Kuis",
            ikon = Icons.Default.PlayArrow,
            warnaBackground = Color(0xFFFFF3E0), // Oranye pastel
            warnaTombol = Color(0xFFFF9800),
            onClick = {}
        )

        // Contoh Penggunaan 2: Simulasi Lab
        CardAksi(
            judul = "Lab Virtual: Reaksi Kimia",
            deskripsi = "Mari mencoba mencampurkan larutan asam dan basa secara virtual.",
            teksTombol = "Masuk Lab",
            ikon = Icons.Default.Science,
            warnaBackground = Color(0xFFE0F2F1), // Hijau tosca pastel
            warnaTombol = Color(0xFF009688),
            onClick = {}
        )
    }
}