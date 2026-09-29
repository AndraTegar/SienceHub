package com.kelompoksix.siencehub.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class KategoriMateri(
    val judul: String,
    val warnaBg: Color,
    val warnaTeks: Color,
    val tinggiCard: Dp // Mengatur tinggi tiap kartu secara dinamis
)