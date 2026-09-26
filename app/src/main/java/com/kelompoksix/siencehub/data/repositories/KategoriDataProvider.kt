package com.kelompoksix.siencehub.data.repositories

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kelompoksix.siencehub.data.models.KategoriMateri

object KategoriDataProvider {
    fun getDaftarKategori(): List<KategoriMateri> {
        return listOf(
            KategoriMateri("FISIKA", Color(0xFF333333), Color.White, 210.dp),     // Panjang
            KategoriMateri("KIMIA", Color(0xFF85D4CC), Color.White, 140.dp),      // Pendek
            KategoriMateri("BIOLOGI", Color(0xFFD3E86E), Color.DarkGray, 200.dp), // Panjang
            KategoriMateri("ASTRONOMI", Color(0xFFF9C846), Color.White, 150.dp),  // Pendek
            KategoriMateri("GEOGRAFI", Color(0xFF1ABC9C), Color.White, 210.dp),   // Panjang
            KategoriMateri("MATEMATIKA", Color(0xFFE67E22), Color.White, 140.dp)  // Pendek
        )
    }
}