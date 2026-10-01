package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.components.PilihanChip
import kotlin.math.abs
import kotlin.math.pow

private data class Zat(val nama: String, val ph: Float)

private val daftarZat = listOf(
    Zat("Jus lemon", 2.0f), Zat("Cuka", 2.9f), Zat("Kopi", 5.0f), Zat("Air murni", 7.0f),
    Zat("Air laut", 8.1f), Zat("Soda kue", 9.0f), Zat("Sabun", 10.0f), Zat("Amonia", 11.5f)
)
private val daftarIndikator = listOf("Kubis ungu", "Lakmus", "Fenolftalein")

private fun warnaIndikator(indikator: String, ph: Float): Color = when (indikator) {
    "Kubis ungu" -> when {
        ph < 3f -> Color(0xFFE53935)
        ph < 5f -> Color(0xFFC2185B)
        ph < 7.5f -> Color(0xFF8E24AA)
        ph < 9f -> Color(0xFF3F51B5)
        ph < 11f -> Color(0xFF26A69A)
        else -> Color(0xFF43A047)
    }
    "Lakmus" -> when {
        ph < 5f -> Color(0xFFD32F2F)
        ph < 8f -> Color(0xFF9C27B0)
        else -> Color(0xFF1E88E5)
    }
    else -> if (ph < 8.2f) Color(0xFFF5F5F5) else Color(0xFFF06292) // fenolftalein
}

@Composable
fun UjiPhScreen(onBackClick: () -> Unit) {
    var zat by remember { mutableStateOf(daftarZat[0]) }
    var indikator by remember { mutableStateOf(daftarIndikator[0]) }
    var sudahDiuji by remember { mutableStateOf(false) }

    val warna = if (sudahDiuji) warnaIndikator(indikator, zat.ph) else Color(0xFFB3E5FC) // air bening kebiruan
    val sifat = when {
        abs(zat.ph - 7f) < 0.05f -> "Netral"
        zat.ph < 7f -> "Asam"
        else -> "Basa"
    }

    EksperimenScaffold("UJI pH INDIKATOR", onBackClick) {
        Text("1. Pilih zat:", fontWeight = FontWeight.SemiBold)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            daftarZat.forEach { z -> PilihanChip(z.nama, z == zat) { zat = z; sudahDiuji = false } }
        }

        Text("2. Pilih indikator:", fontWeight = FontWeight.SemiBold)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            daftarIndikator.forEach { i -> PilihanChip(i, i == indikator) { indikator = i; sudahDiuji = false } }
        }

        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEF7))) {
            Canvas(Modifier.fillMaxWidth().height(170.dp)) {
                val w = 110.dp.toPx()
                val h = 140.dp.toPx()
                val kiri = (size.width - w) / 2
                val atas = 15.dp.toPx()
                drawRoundRect(warna, Offset(kiri + 3, atas + h * 0.35f), Size(w - 6, h * 0.65f - 3), CornerRadius(12.dp.toPx()))
                drawRoundRect(Color.Gray, Offset(kiri, atas), Size(w, h), CornerRadius(12.dp.toPx()), style = Stroke(4.dp.toPx()))
            }
        }

        Button(
            onClick = { sudahDiuji = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D9078))
        ) { Text("Teteskan indikator") }

        if (sudahDiuji) {
            val pOH = 14f - zat.ph
            val hplus = 10.0.pow(-zat.ph.toDouble())
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFC9DDF5))) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${zat.nama}: pH ${"%.1f".format(zat.ph)} ($sifat)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                    Text("pOH = 14 − pH = ${"%.1f".format(pOH)}", fontSize = 14.sp, color = Color.DarkGray)
                    Text("[H⁺] = 10^−pH = ${"%.1e".format(hplus)} M", fontSize = 14.sp, color = Color.DarkGray)
                    if (indikator == "Fenolftalein") {
                        Text("Fenolftalein hanya berwarna (merah muda) pada basa, tak berwarna pada asam dan netral.", fontSize = 13.sp, color = Color.DarkGray)
                    }
                }
            }
        } else {
            Text("Pilih zat dan indikator, lalu teteskan untuk melihat perubahan warna.", fontSize = 13.sp, color = Color.DarkGray)
        }
    }
}