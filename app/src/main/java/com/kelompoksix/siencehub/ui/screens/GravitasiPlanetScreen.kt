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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.components.LabMetricItem
import com.kelompoksix.siencehub.ui.components.LabMetricRow
import com.kelompoksix.siencehub.ui.components.PilihanChip
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import kotlin.math.sqrt

private data class Planet(val nama: String, val g: Float, val warna: Color)

private val daftarPlanet = listOf(
    Planet("Bulan", 1.62f, Color(0xFFCBD5E1)),
    Planet("Merkurius", 3.70f, Color(0xFF94A3B8)),
    Planet("Mars", 3.71f, Color(0xFFEA580C)),
    Planet("Venus", 8.87f, Color(0xFFFBBF24)),
    Planet("Uranus", 8.87f, Color(0xFF22D3EE)),
    Planet("Bumi", 9.81f, Color(0xFF3B82F6)),
    Planet("Saturnus", 10.44f, Color(0xFFD6B370)),
    Planet("Neptunus", 11.15f, Color(0xFF6366F1)),
    Planet("Jupiter", 24.79f, Color(0xFFD97706))
)

@Composable
fun GravitasiPlanetScreen(onBackClick: () -> Unit) {
    val metrics = rememberResponsiveMetrics()
    val spacePurple = Color(0xFF6D28D9)
    val tinggi = 20f

    var planet by remember { mutableStateOf(daftarPlanet[5]) }
    var massa by remember { mutableFloatStateOf(50f) }
    var jatuh by remember { mutableStateOf(false) }
    var jarak by remember { mutableFloatStateOf(0f) }
    var kecepatan by remember { mutableFloatStateOf(0f) }
    var waktu by remember { mutableFloatStateOf(0f) }

    fun reset() {
        jatuh = false
        jarak = 0f
        kecepatan = 0f
        waktu = 0f
    }

    LaunchedEffect(jatuh) {
        if (!jatuh) return@LaunchedEffect
        var terakhir = withFrameNanos { it }
        while (jatuh) {
            val sekarang = withFrameNanos { it }
            val dt = ((sekarang - terakhir) / 1_000_000_000f).coerceAtMost(0.05f)
            terakhir = sekarang
            waktu += dt
            kecepatan += planet.g * dt
            jarak += kecepatan * dt
            if (jarak >= tinggi) {
                jarak = tinggi
                waktu = sqrt(2 * tinggi / planet.g)
                jatuh = false
            }
        }
    }

    val waktuTeoritis = sqrt(2 * tinggi / planet.g)

    EksperimenScaffold(
        judul = "GRAVITASI PLANET",
        subjudul = "Gerak Jatuh Bebas dari Ketinggian 20 Meter",
        headerColor = spacePurple,
        onBackClick = onBackClick
    ) {
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            daftarPlanet.forEach { p ->
                PilihanChip(p.nama, p == planet, accentColor = spacePurple) {
                    planet = p
                    reset()
                }
            }
        }

        LabMetricRow(
            items = listOf(
                LabMetricItem(
                    label = "Gravitasi (${planet.nama})",
                    value = "${"%.2f".format(planet.g)} m/s²",
                    highlightColor = spacePurple
                ),
                LabMetricItem(
                    label = "Waktu Jatuh",
                    value = "${"%.2f".format(if (waktu > 0f) waktu else waktuTeoritis)} s"
                ),
                LabMetricItem(
                    label = "Berat Benda",
                    value = "${"%.0f".format(massa * planet.g)} N",
                    highlightColor = Color(0xFFD97706)
                )
            )
        )

        Card(
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(if (metrics.isShortScreen) 185.dp else 215.dp)
            ) {
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                    )
                )

                // Titik-titik bintang dekoratif
                val starCoords = listOf(
                    0.15f to 0.20f, 0.30f to 0.12f, 0.72f to 0.18f,
                    0.85f to 0.32f, 0.22f to 0.55f, 0.78f to 0.60f
                )
                starCoords.forEach { (sx, sy) ->
                    drawCircle(
                        color = Color.White.copy(alpha = 0.45f),
                        radius = 1.8.dp.toPx(),
                        center = Offset(size.width * sx, size.height * sy)
                    )
                }

                val tanah = size.height - 20.dp.toPx()
                val r = 15.dp.toPx()

                // Garis penggaris tinggi di kiri
                val rulerX = 28.dp.toPx()
                drawLine(
                    color = Color.White.copy(alpha = 0.28f),
                    start = Offset(rulerX, r),
                    end = Offset(rulerX, tanah - r),
                    strokeWidth = 1.5.dp.toPx()
                )
                for (step in 0..4) {
                    val ty = r + (step / 4f) * (tanah - 2 * r)
                    drawLine(
                        color = Color.White.copy(alpha = 0.45f),
                        start = Offset(rulerX - 4.dp.toPx(), ty),
                        end = Offset(rulerX + 4.dp.toPx(), ty),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }

                // Permukaan planet
                drawLine(
                    color = planet.warna,
                    start = Offset(16.dp.toPx(), tanah),
                    end = Offset(size.width - 16.dp.toPx(), tanah),
                    strokeWidth = 6.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Bola uji jatuh bebas
                val y = r + (jarak / tinggi) * (tanah - 2 * r)
                drawCircle(
                    color = planet.warna.copy(alpha = 0.28f),
                    radius = r * 1.45f,
                    center = Offset(size.width / 2f, y)
                )
                drawCircle(
                    color = planet.warna,
                    radius = r,
                    center = Offset(size.width / 2f, y)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = spacePurple.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "t = √(2h / g) = ${"%.2f".format(waktuTeoritis)} s   •   Kecepatan saat ini: ${"%.1f".format(kecepatan)} m/s",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = spacePurple,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Massa Benda (m)", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TextPrimary)
                    Text("${massa.toInt()} kg", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = spacePurple)
                }
                Slider(
                    value = massa,
                    onValueChange = { massa = it },
                    valueRange = 1f..100f,
                    colors = SliderDefaults.colors(thumbColor = spacePurple, activeTrackColor = spacePurple)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { if (jarak >= tinggi) reset(); jatuh = true },
                        enabled = !jatuh,
                        colors = ButtonDefaults.buttonColors(containerColor = spacePurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Jatuhkan Benda", fontWeight = FontWeight.Bold) }
                    OutlinedButton(
                        onClick = { reset() },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.weight(1f)
                    ) { Text("Reset", color = TextPrimary, fontWeight = FontWeight.SemiBold) }
                }
            }
        }
    }
}
