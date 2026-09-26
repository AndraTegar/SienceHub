package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardStatus(
    level: Int,
    streak: Int
) {
    // Warna card yang sedikit lebih abu-abu/gelap dari background utama
    val warnaCard = Color(0xFFA1B09C)

    Card(
        shape = RoundedCornerShape(24.dp), // Sudut melengkung menyerupai kapsul
        colors = CardDefaults.cardColors(containerColor = warnaCard),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp) // Tinggi fix untuk kartu
    ) {
        // Row utama
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ==========================================
            // BAGIAN KIRI: LEVEL
            // ==========================================
            Row(
                modifier = Modifier
                    .weight(1f) // Mengambil 50% ruang horizontal
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Level",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Lingkaran untuk angka level dengan border putih tipis
                Surface(
                    shape = CircleShape,
                    color = Color.Transparent,
                    border = BorderStroke(2.dp, Color.White),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = level.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // ==========================================
            // GARIS PEMISAH (DIVIDER)
            // ==========================================
            VerticalDivider(
                color = Color.DarkGray.copy(alpha = 0.3f), // Warna abu-abu transparan
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 12.dp) // Memberi jarak dari atas & bawah kartu
            )

            // ==========================================
            // BAGIAN KANAN: STREAK
            // ==========================================
            Row(
                modifier = Modifier
                    .weight(1f) // Mengambil 50% ruang horizontal sisanya
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Streak",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Menampilkan angka streak (Bisa diganti ikon api nanti)
                Text(
                    text = "$streak Hari",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF869E83)
@Composable
fun CardStatusPreview() {
    // Dibungkus dengan padding agar tidak menempel di tepi layar saat preview
    Box(modifier = Modifier.padding(16.dp)) {
        CardStatus(level = 1, streak = 5)
    }
}