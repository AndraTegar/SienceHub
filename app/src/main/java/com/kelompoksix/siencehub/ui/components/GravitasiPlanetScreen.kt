package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.components.PilihanChip
import kotlin.math.sqrt

private data class Planet(val nama: String, val g: Float, val warna: Color)

private val daftarPlanet = listOf(
    Planet("Bulan", 1.62f, Color(0xFFBDBDBD)),
    Planet("Merkurius", 3.70f, Color(0xFF9E9E9E)),
    Planet("Mars", 3.71f, Color(0xFFE64A19)),
    Planet("Venus", 8.87f, Color(0xFFFFB74D)),
    Planet("Uranus", 8.87f, Color(0xFF4DD0E1)),
    Planet("Bumi", 9.81f, Color(0xFF42A5F5)),
    Planet("Saturnus", 10.44f, Color(0xFFD4B483)),
    Planet("Neptunus", 11.15f, Color(0xFF3F51B5)),
    Planet("Jupiter", 24.79f, Color(0xFFC08552))
)

@Composable
fun GravitasiPlanetScreen(onBackClick: () -> Unit) {
    val tinggi = 20f // meter
    var planet by remember { mutableStateOf(daftarPlanet[5]) } // Bumi
    var massa by remember { mutableFloatStateOf(50f) }
    var jatuh by remember { mutableStateOf(false) }
    var jarak by remember { mutableFloatStateOf(0f) }
    var kecepatan by remember { mutableFloatStateOf(0f) }
    var waktu by remember { mutableFloatStateOf(0f) }

    fun reset() { jatuh = false; jarak = 0f; kecepatan = 0f; waktu = 0f }

    LaunchedEffect(jatuh) {
        if (!jatuh) return@LaunchedEffect
        var terakhir = withFrameNanos { it }
        while (jatuh) {
            val sekarang = withFrameNanos { it }
            val dt = (sekarang - terakhir) / 1_000_000_000f
            terakhir = sekarang
            waktu += dt
            kecepatan += planet.g * dt
            jarak += kecepatan * dt
            if (jarak >= tinggi) { jarak = tinggi; jatuh = false }
        }
    }

    EksperimenScaffold("GRAVITASI PLANET", onBackClick) {
        Text("Pilih planet:", fontWeight = FontWeight.SemiBold)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            daftarPlanet.forEach { p ->
                PilihanChip(p.nama, p == planet) { planet = p; reset() }
            }
        }

        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A44))) {
            Canvas(Modifier.fillMaxWidth().height(220.dp).padding(horizontal = 12.dp)) {
                val tanah = size.height - 16.dp.toPx()
                val r = 16.dp.toPx()
                drawLine(Color(0xFF8D6E63), Offset(0f, tanah), Offset(size.width, tanah), 6.dp.toPx())
                val y = r + (jarak / tinggi) * (tanah - 2 * r)
                drawCircle(planet.warna, r, Offset(size.width / 2, y))
            }
        }

        val waktuJatuh = sqrt(2 * tinggi / planet.g)
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFC9DDF5))) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("g di ${planet.nama} = ${"%.2f".format(planet.g)} m/s²", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                Text("Waktu jatuh dari ${tinggi.toInt()} m: ${"%.2f".format(waktuJatuh)} s", fontSize = 14.sp, color = Color.DarkGray)
                Text("Waktu berjalan: ${"%.2f".format(waktu)} s   |   Kecepatan: ${"%.1f".format(kecepatan)} m/s", fontSize = 14.sp, color = Color.DarkGray)
                Text("Berat ${massa.toInt()} kg di sini: ${"%.0f".format(massa * planet.g)} N", fontSize = 14.sp, color = Color.DarkGray)
            }
        }

        Text("Massa benda: ${massa.toInt()} kg", fontWeight = FontWeight.SemiBold)
        Slider(value = massa, onValueChange = { massa = it }, valueRange = 1f..100f)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { if (jarak >= tinggi) reset(); jatuh = true },
                enabled = !jatuh,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D9078))
            ) { Text("Jatuhkan") }
            OutlinedButton(onClick = { reset() }) { Text("Reset") }
        }

        Text(
            "Ubah massa: waktu jatuh tetap sama, hanya beratnya yang berubah. Bandingkan Bulan dan Jupiter.",
            fontSize = 13.sp, color = Color.DarkGray
        )
    }
}