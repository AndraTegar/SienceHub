package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.BorderStroke
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
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary

@Composable
fun CardAksi(
    judul: String,
    deskripsi: String,
    teksTombol: String,
    ikon: ImageVector,
    warnaBackground: Color = SageSoft,
    warnaTombol: Color = SagePrimary,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = warnaBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, warnaTombol.copy(alpha = 0.14f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = judul,
                    color = TextPrimary,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = deskripsi,
                    color = TextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = warnaTombol),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = teksTombol,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceWhite.copy(alpha = 0.78f),
                modifier = Modifier.size(66.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = ikon,
                        contentDescription = null,
                        tint = warnaTombol,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardAksiPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        CardAksi(
            judul = "Tantangan Harian \uD83D\uDD25",
            deskripsi = "Selesaikan 5 soal kuis biologi berturut-turut tanpa salah untuk mendapat bonus +50 XP.",
            teksTombol = "Mulai Kuis",
            ikon = Icons.Default.PlayArrow,
            warnaBackground = Color(0xFFFFF3E0),
            warnaTombol = Color(0xFFFF9800),
            onClick = {}
        )
        CardAksi(
            judul = "Lab Virtual: Reaksi Kimia",
            deskripsi = "Mari mencoba mencampurkan larutan asam dan basa secara virtual.",
            teksTombol = "Masuk Lab",
            ikon = Icons.Default.Science,
            warnaBackground = Color(0xFFE0F2F1),
            warnaTombol = Color(0xFF009688),
            onClick = {}
        )
    }
}
