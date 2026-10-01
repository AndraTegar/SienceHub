package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimulatorNewtonScreen(onBackClick: () -> Unit) {
    val kuning = Color(0xFFFFC94D)
    val hijau = Color(0xFF7D9078)
    val panjangLintasan = 20f // meter

    var gaya by remember { mutableFloatStateOf(20f) }   // N
    var massa by remember { mutableFloatStateOf(5f) }   // kg
    var jalan by remember { mutableStateOf(false) }
    var posisi by remember { mutableFloatStateOf(0f) }
    var kecepatan by remember { mutableFloatStateOf(0f) }

    val a = gaya / massa

    LaunchedEffect(jalan) {
        if (!jalan) return@LaunchedEffect
        var terakhir = withFrameNanos { it }
        while (jalan) {
            val sekarang = withFrameNanos { it }
            val dt = (sekarang - terakhir) / 1_000_000_000f
            terakhir = sekarang
            kecepatan += a * dt
            posisi += kecepatan * dt
            if (posisi >= panjangLintasan) {
                posisi = panjangLintasan
                jalan = false
            }
        }
    }

    fun reset() { jalan = false; posisi = 0f; kecepatan = 0f }

    Column(Modifier.fillMaxSize().background(kuning)) {
        Spacer(Modifier.height(44.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp), contentAlignment = Alignment.Center) {
            Row(
                Modifier.fillMaxWidth().height(48.dp)
                    .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(50.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, "Kembali", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Text("HUKUM II NEWTON", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }

        Spacer(Modifier.height(28.dp))

        Surface(
            Modifier.fillMaxWidth().weight(1f),
            color = Color(0xFFF9F7FF),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ---------- Arena ----------
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEF7))
                ) {
                    Canvas(Modifier.fillMaxWidth().height(170.dp).padding(horizontal = 12.dp)) {
                        val lantai = size.height - 24.dp.toPx()
                        drawLine(Color.DarkGray, Offset(0f, lantai), Offset(size.width, lantai), 4.dp.toPx())

                        val sisi = (30f + massa * 2f).dp.toPx()
                        val x = (posisi / panjangLintasan) * (size.width - sisi)
                        drawRect(hijau, Offset(x, lantai - sisi), Size(sisi, sisi))

                        // panah gaya (di atas balok)
                        val yPanah = lantai - sisi - 16.dp.toPx()
                        val panjangPanah = (16f + gaya * 1.4f).dp.toPx()
                        val ujung = (x + panjangPanah).coerceAtMost(size.width)
                        val tebal = 4.dp.toPx()
                        drawLine(Color.Red, Offset(x, yPanah), Offset(ujung, yPanah), tebal)
                        drawLine(Color.Red, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah - 6.dp.toPx()), tebal)
                        drawLine(Color.Red, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah + 6.dp.toPx()), tebal)
                    }
                }

                // ---------- Hasil ----------
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFC9DDF5))
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "a = F / m = ${"%.0f".format(gaya)} / ${"%.0f".format(massa)} = ${"%.1f".format(a)} m/s²",
                            fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A)
                        )
                        Text("Kecepatan: ${"%.1f".format(kecepatan)} m/s", fontSize = 14.sp, color = Color.DarkGray)
                        Text("Jarak: ${"%.1f".format(posisi)} m / ${panjangLintasan.toInt()} m", fontSize = 14.sp, color = Color.DarkGray)
                    }
                }

                // ---------- Kontrol ----------
                Text("Gaya (F): ${"%.0f".format(gaya)} N", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = gaya, onValueChange = { gaya = it; reset() },
                    valueRange = 1f..100f, enabled = !jalan
                )

                Text("Massa (m): ${"%.0f".format(massa)} kg", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = massa, onValueChange = { massa = it; reset() },
                    valueRange = 1f..20f, enabled = !jalan
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { if (posisi >= panjangLintasan) reset(); jalan = !jalan },
                        colors = ButtonDefaults.buttonColors(containerColor = hijau)
                    ) { Text(if (jalan) "Jeda" else "Jalankan") }
                    OutlinedButton(onClick = { reset() }) { Text("Reset") }
                }

                Text(
                    "Coba: gandakan gaya, percepatan ikut 2x. Gandakan massa, percepatan jadi setengah.",
                    fontSize = 13.sp, color = Color.DarkGray
                )
            }
        }
    }
}