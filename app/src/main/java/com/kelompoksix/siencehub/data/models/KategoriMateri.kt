package com.kelompoksix.siencehub.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class KategoriMateri(
    val judul: String,
    val warnaBg: Color,
    val warnaTeks: Color,
    val tinggiCard: Dp,
    val resId: Int? = null,
    val gambarSize: Dp = 64.dp // <-- Parameter baru untuk mengatur ukuran gambar per kartu
)