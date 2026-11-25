package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
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
import com.kelompoksix.siencehub.ui.theme.AccentBlue
import com.kelompoksix.siencehub.ui.theme.AccentBlueSoft
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
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
    val tinggi = 20f
    var planet by remember { mutableStateOf(daftarPlanet[5]) }
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
        Text("Pilih planet:", fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            daftarPlanet.forEach { p ->
                PilihanChip(p.nama, p == planet) { planet = p; reset() }
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2A44))
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .padding(horizontal = 12.dp)
            ) {
                val tanah = size.height - 16.dp.toPx()
                val r = 16.dp.toPx()
                drawLine(Color(0xFF8D6E63), Offset(0f, tanah), Offset(size.width, tanah), 6.dp.toPx())
                val y = r + (jarak / tinggi) * (tanah - 2 * r)
                drawCircle(planet.warna, r, Offset(size.width / 2, y))
            }
        }

        val waktuJatuh = sqrt(2 * tinggi / planet.g)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AccentBlueSoft)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "g di ${planet.nama} = ${"%.2f".format(planet.g)} m/s²",
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                )
                Text("Waktu jatuh dari ${tinggi.toInt()} m: ${"%.2f".format(waktuJatuh)} s", fontSize = 13.5.sp, color = TextPrimary)
                Text("Waktu berjalan: ${"%.2f".format(waktu)} s   •   Kecepatan: ${"%.1f".format(kecepatan)} m/s", fontSize = 13.5.sp, color = TextPrimary)
                Text("Berat ${massa.toInt()} kg di sini: ${"%.0f".format(massa * planet.g)} N", fontSize = 13.5.sp, color = TextPrimary)
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Massa benda: ${massa.toInt()} kg", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Slider(
                    value = massa,
                    onValueChange = { massa = it },
                    valueRange = 1f..100f,
                    colors = SliderDefaults.colors(thumbColor = SagePrimary, activeTrackColor = SagePrimary)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { if (jarak >= tinggi) reset(); jatuh = true },
                        enabled = !jatuh,
                        colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Jatuhkan") }
                    OutlinedButton(
                        onClick = { reset() },
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Reset") }
                }
            }
        }

        Text(
            "Ubah massa: waktu jatuh tetap sama, hanya beratnya yang berubah. Bandingkan Bulan dan Jupiter.",
            fontSize = 12.5.sp,
            color = TextSecondary
        )
    }
}
