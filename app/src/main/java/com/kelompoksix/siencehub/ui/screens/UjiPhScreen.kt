package com.kelompoksix.siencehub.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.components.LabMetricItem
import com.kelompoksix.siencehub.ui.components.LabMetricRow
import com.kelompoksix.siencehub.ui.components.PilihanChip
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.SurfaceMuted
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import kotlin.math.abs
import kotlin.math.pow

private data class Zat(val nama: String, val ph: Float)

private val daftarZat = listOf(
    Zat("Jus Lemon", 2.0f),
    Zat("Cuka Dapur", 2.9f),
    Zat("Kopi Hitam", 5.0f),
    Zat("Air Murni", 7.0f),
    Zat("Air Laut", 8.1f),
    Zat("Soda Kue", 9.0f),
    Zat("Air Sabun", 10.0f),
    Zat("Amonia", 11.5f)
)

private val daftarIndikator = listOf("Kubis Ungu", "Lakmus", "Fenolftalein")

private fun warnaIndikator(indikator: String, ph: Float): Color = when (indikator) {
    "Kubis Ungu" -> when {
        ph < 3f -> Color(0xFFE53935)
        ph < 5f -> Color(0xFFC2185B)
        ph < 7.5f -> Color(0xFF8E24AA)
        ph < 9f -> Color(0xFF3F51B5)
        ph < 11f -> Color(0xFF26A69A)
        else -> Color(0xFF43A047)
    }
    "Lakmus" -> when {
        ph < 6.5f -> Color(0xFFEF4444)
        ph <= 7.5f -> Color(0xFFA855F7)
        else -> Color(0xFF2563EB)
    }
    else -> if (ph < 8.2f) Color(0xFFE2E8F0) else Color(0xFFEC4899)
}

@Composable
fun UjiPhScreen(onBackClick: () -> Unit) {
    val metrics = rememberResponsiveMetrics()
    val chemTeal = Color(0xFF0891B2)

    var zat by remember { mutableStateOf(daftarZat[0]) }
    var indikator by remember { mutableStateOf(daftarIndikator[0]) }
    var sudahDiuji by remember { mutableStateOf(false) }

    val targetLiquidColor = if (sudahDiuji) warnaIndikator(indikator, zat.ph) else Color(0xFFbae6fd)
    val animatedLiquidColor by animateColorAsState(
        targetValue = targetLiquidColor,
        animationSpec = tween(450),
        label = "liquidColor"
    )

    val sifat = when {
        abs(zat.ph - 7f) < 0.05f -> "Netral"
        zat.ph < 7f -> "Asam"
        else -> "Basa"
    }
    val pOH = 14f - zat.ph
    val hplus = 10.0.pow(-zat.ph.toDouble())

    EksperimenScaffold(
        judul = "UJI pH INDIKATOR",
        subjudul = "Identifikasi Larutan Asam, Netral, & Basa",
        headerColor = chemTeal,
        onBackClick = onBackClick
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("1. Pilih sampel larutan:", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TextPrimary)
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    daftarZat.forEach { z ->
                        PilihanChip(z.nama, z == zat, accentColor = chemTeal) {
                            zat = z
                            sudahDiuji = false
                        }
                    }
                }

                Text("2. Pilih jenis indikator:", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TextPrimary)
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    daftarIndikator.forEach { i ->
                        PilihanChip(i, i == indikator, accentColor = chemTeal) {
                            indikator = i
                            sudahDiuji = false
                        }
                    }
                }
            }
        }

        // Visualisasi Gelas Kimia + Spektrum Skala pH 0-14
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceMuted),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(if (metrics.isShortScreen) 150.dp else 170.dp)
                ) {
                    val w = 112.dp.toPx()
                    val h = size.height - 20.dp.toPx()
                    val kiri = (size.width - w) / 2f
                    val atas = 10.dp.toPx()

                    // Larutan di dalam gelas ukur
                    drawRoundRect(
                        color = animatedLiquidColor,
                        topLeft = Offset(kiri + 4.dp.toPx(), atas + h * 0.30f),
                        size = Size(w - 8.dp.toPx(), h * 0.70f - 4.dp.toPx()),
                        cornerRadius = CornerRadius(12.dp.toPx())
                    )

                    // Garis takaran gelas kimia
                    for (line in 1..3) {
                        val ly = atas + h * (0.25f + line * 0.16f)
                        drawLine(
                            color = Color(0xFF64748B).copy(alpha = 0.55f),
                            start = Offset(kiri + w - 22.dp.toPx(), ly),
                            end = Offset(kiri + w - 6.dp.toPx(), ly),
                            strokeWidth = 1.8.dp.toPx()
                        )
                    }

                    // Dinding kaca gelas ukur
                    drawRoundRect(
                        color = Color(0xFF64748B),
                        topLeft = Offset(kiri, atas),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(14.dp.toPx()),
                        style = Stroke(3.5.dp.toPx())
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Pita Spektrum pH 0 - 14 dengan Penanda Posisi Larutan Saat Dihitung
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0 (Asam Kuat)", fontSize = 10.5.sp, color = TextSecondary)
                        Text("7 (Netral)", fontSize = 10.5.sp, color = TextSecondary)
                        Text("14 (Basa Kuat)", fontSize = 10.5.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(4.dp))
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        val barWidth = maxWidth
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color(0xFFEF4444),
                                            Color(0xFFF97316),
                                            Color(0xFFEAB308),
                                            Color(0xFF22C55E),
                                            Color(0xFF06B6D4),
                                            Color(0xFF3B82F6),
                                            Color(0xFF7C3AED)
                                        )
                                    )
                                )
                        )
                        if (sudahDiuji) {
                            val markerOffset = (barWidth - 14.dp) * (zat.ph / 14f).coerceIn(0f, 1f)
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                border = BorderStroke(2.5.dp, TextPrimary),
                                modifier = Modifier
                                    .offset(x = markerOffset, y = (-2).dp)
                                    .size(14.dp)
                            ) {}
                        }
                    }
                }
            }
        }

        Button(
            onClick = { sudahDiuji = true },
            colors = ButtonDefaults.buttonColors(containerColor = chemTeal),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = if (sudahDiuji) "Indikator Diteteskan (${zat.nama})" else "Teteskan Indikator $indikator",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (sudahDiuji) {
            LabMetricRow(
                items = listOf(
                    LabMetricItem("Nilai pH", "${"%.1f".format(zat.ph)} ($sifat)", chemTeal),
                    LabMetricItem("Nilai pOH", "%.1f".format(pOH)),
                    LabMetricItem("[H⁺]", "%.1e M".format(hplus))
                )
            )

            if (indikator == "Fenolftalein") {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = chemTeal.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Catatan: Fenolftalein berubah merah muda pada basa (pH ≥ 8,2) dan tetap jernih pada larutan asam maupun netral.",
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }
}
