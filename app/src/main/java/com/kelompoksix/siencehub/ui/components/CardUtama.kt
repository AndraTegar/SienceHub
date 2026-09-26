package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardUtama(
    judulMateri: String,
    progress: Float, // Nilai dari 0.0f (0%) sampai 1.0f (100%)
    onClick: () -> Unit
) {
    val warnaBeige = Color(0xFFE2D4C0)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = warnaBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // BAGIAN ATAS: Label kecil dan Judul Materi
            Column {
                // Label kecil penanda bagian
                Text(
                    text = "LANJUTKAN BELAJAR",
                    color = Color.DarkGray.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = judulMateri,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // BAGIAN BAWAH: Progress Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = Color(0xFF869E83),
                    trackColor = Color.White,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF869E83)
@Composable
fun CardUtamaPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        CardUtama(
            judulMateri = "Fisika: Hukum Newton",
            progress = 0.75f,
            onClick = {}
        )
    }
}