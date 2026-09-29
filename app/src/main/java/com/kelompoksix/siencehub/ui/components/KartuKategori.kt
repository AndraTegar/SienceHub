package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.KategoriMateri

@Composable
fun KartuKategori(
    item: KategoriMateri,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = item.warnaBg),
        modifier = Modifier
            .fillMaxWidth()
            .height(item.tinggiCard)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Menampilkan gambar jika resId tidak null, ukurannya bisa diatur per kartu
            if (item.resId != null) {
                Image(
                    painter = painterResource(id = item.resId),
                    contentDescription = item.judul,
                    modifier = Modifier
                        .size(item.gambarSize) // <-- Menggunakan ukuran dinamis dari data
                        .align(Alignment.Center), // Bisa diubah ke Alignment.TopEnd atau Center sesuai selera
                    contentScale = ContentScale.Fit
                )
            }

            // Teks judul di bagian bawah
            Text(
                text = item.judul,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = item.warnaTeks,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}
