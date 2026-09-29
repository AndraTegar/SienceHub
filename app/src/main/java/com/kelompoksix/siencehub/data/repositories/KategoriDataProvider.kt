package com.kelompoksix.siencehub.data.repositories

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kelompoksix.siencehub.R
import com.kelompoksix.siencehub.data.models.KategoriMateri

object KategoriDataProvider {
    fun getDaftarKategori(): List<KategoriMateri> {
        return listOf(
            KategoriMateri(
                judul = "FISIKA",
                warnaBg = Color(0xFFABA7A7),
                warnaTeks = Color.White,
                tinggiCard = 210.dp,
                resId = R.drawable.fisika_icon,
                gambarSize = 80.dp // <-- Ukuran gambar Fisika bisa diatur di sini
            ),
            KategoriMateri(
                judul = "KIMIA",
                warnaBg = Color(0xFF85D4CC),
                warnaTeks = Color.White,
                tinggiCard = 160.dp,
                resId = R.drawable.kimia_icon,
                gambarSize = 65.dp // <-- Ukuran gambar Kimia bisa dibikin lebih kecil/besar bebas
            ),
            KategoriMateri(
                judul = "BIOLOGI",
                warnaBg = Color(0xFFD3E86E),
                warnaTeks = Color.DarkGray,
                tinggiCard = 200.dp,
                resId = R.drawable.biologi,
                gambarSize = 64.dp
            ),
            KategoriMateri(
                judul = "ASTRONOMI",
                warnaBg = Color(0xFFF9C846),
                warnaTeks = Color.White,
                tinggiCard = 150.dp,
                resId = R.drawable.astronomy,
                gambarSize = 64.dp
            ),
            KategoriMateri(
                judul = "GEOGRAFI",
                warnaBg = Color(0xFF1ABC9C),
                warnaTeks = Color.White,
                tinggiCard = 210.dp,
                resId = R.drawable.geo,
                gambarSize = 64.dp
            ),
            KategoriMateri(
                judul = "MATH",
                warnaBg = Color(0xFFE67E22),
                warnaTeks = Color.White,
                tinggiCard = 200.dp,
                resId = R.drawable.mtk,
                gambarSize = 64.dp
            )
        )
    }
}