package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.repositories.KategoriDataProvider
import com.kelompoksix.siencehub.ui.components.KartuKategori

@Composable
fun MateriScreen(
    onMateriClick: (String) -> Unit = {}
) {
    val warnaHijauSage = Color(0xFF7A8B76)
    val daftarKategori = KategoriDataProvider.getDaftarKategori()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(warnaHijauSage)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Sapaan Header
        Text(
            text = "Halo, Ridho",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Mau belajar apa hari ini?",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.85f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Grid Asimetris (Panjang dan Pendek ala Figma)
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            items(daftarKategori) { kategori ->
                KartuKategori(
                    item = kategori,
                    onClick = { onMateriClick(kategori.judul) }
                )
            }
        }
    }
}