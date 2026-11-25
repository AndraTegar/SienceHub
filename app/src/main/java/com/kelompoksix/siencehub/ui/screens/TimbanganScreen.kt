package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.components.LabMetricItem
import com.kelompoksix.siencehub.ui.components.LabMetricRow
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.StatusSuccess
import com.kelompoksix.siencehub.ui.theme.SurfaceMuted
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

private data class SoalLinear(val a: Int, val b: Int, val c: Int)

private fun buatSoal(): SoalLinear {
    val x = Random.nextInt(1, 12)
    val a = Random.nextInt(2, 6)
    val b = Random.nextInt(1, 10)
    return SoalLinear(a, b, a * x + b)
}

@Composable
fun TimbanganScreen(onBackClick: () -> Unit) {
    val metrics = rememberResponsiveMetrics()
    val mathAmber = Color(0xFFD97706)

    var soal by remember { mutableStateOf(buatSoal()) }
    var tebakan by remember { mutableFloatStateOf(0f) }
    var jumlahSelesai by remember { mutableIntStateOf(0) }
    var sudahDihitungSelesai by remember { mutableStateOf(false) }

    val x = tebakan.roundToInt()
    val kiri = soal.a * x + soal.b
    val kanan = soal.c
    val seimbang = kiri == kanan

    LaunchedEffect(seimbang, soal) {
        if (seimbang && !sudahDihitungSelesai) {
            sudahDihitungSelesai = true
            jumlahSelesai++
        }
    }

    EksperimenScaffold(
        judul = "TIMBANGAN PERSAMAAN",
        subjudul = "Kesetimbangan Persamaan Linear Satu Variabel",
        headerColor = mathAmber,
        onBackClick = onBackClick
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, if (seimbang) StatusSuccess.copy(alpha = 0.5f) else BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Temukan nilai x agar kedua lengan timbangan setimbang:",
                    fontSize = 12.5.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "${soal.a}x + ${soal.b} = ${soal.c}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (seimbang) StatusSuccess else mathAmber
                )
            }
        }

        LabMetricRow(
            items = listOf(
                LabMetricItem("Ruas Kiri", "$kiri", if (seimbang) StatusSuccess else null),
                LabMetricItem("Ruas Kanan", "$kanan", if (seimbang) StatusSuccess else null),
                LabMetricItem("Tuntas", "$jumlahSelesai soal", mathAmber)
            )
        )

        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceMuted),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(if (metrics.isShortScreen) 165.dp else 190.dp)
            ) {
                val cx = size.width / 2f
                val cy = 58.dp.toPx()
                val panjang = size.width * 0.35f
                val sudut = ((kanan - kiri).coerceIn(-30, 30) / 30f) * 15f
                val rad = Math.toRadians(sudut.toDouble())
                val dx = (panjang * cos(rad)).toFloat()
                val dy = (panjang * sin(rad)).toFloat()
                val ujungKiri = Offset(cx - dx, cy - dy)
                val ujungKanan = Offset(cx + dx, cy + dy)
                val dasar = size.height - 14.dp.toPx()

                // Tiang penyangga neraca
                drawLine(Color(0xFF64748B), Offset(cx, cy), Offset(cx, dasar), 6.dp.toPx(), cap = StrokeCap.Round)
                drawLine(Color(0xFF64748B), Offset(cx - 52.dp.toPx(), dasar), Offset(cx + 52.dp.toPx(), dasar), 6.dp.toPx(), cap = StrokeCap.Round)

                // Lengan timbangan
                drawLine(
                    color = if (seimbang) Color(0xFF16A34A) else mathAmber,
                    start = ujungKiri,
                    end = ujungKanan,
                    strokeWidth = 7.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Piringan & beban kiri-kanan
                listOf(ujungKiri to true, ujungKanan to false).forEach { (p, isLeft) ->
                    val panY = p.y + 38.dp.toPx()
                    drawLine(Color(0xFF94A3B8), p, Offset(p.x - 24.dp.toPx(), panY), 2.dp.toPx())
                    drawLine(Color(0xFF94A3B8), p, Offset(p.x + 24.dp.toPx(), panY), 2.dp.toPx())

                    // Beban visual di atas piringan
                    val blockW = 42.dp.toPx()
                    val blockH = 22.dp.toPx()
                    drawRoundRect(
                        color = if (isLeft) mathAmber else Color(0xFF3B82F6),
                        topLeft = Offset(p.x - blockW / 2f, panY - blockH),
                        size = Size(blockW, blockH),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )

                    drawRoundRect(
                        color = if (seimbang) Color(0xFF22C55E) else Color(0xFFEF4444),
                        topLeft = Offset(p.x - 38.dp.toPx(), panY),
                        size = Size(76.dp.toPx(), 9.dp.toPx()),
                        cornerRadius = CornerRadius(5.dp.toPx())
                    )
                }
                drawCircle(Color(0xFF334155), 8.dp.toPx(), Offset(cx, cy))
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (seimbang) StatusSuccess.copy(alpha = 0.14f) else mathAmber.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (seimbang) {
                    Text(
                        text = "Seimbang Sempurna! Nilai x = $x ✓",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess
                    )
                    Text(
                        text = "Langkah: ${soal.a}x = ${soal.c} − ${soal.b} = ${soal.c - soal.b}  →  x = ${(soal.c - soal.b) / soal.a}",
                        fontSize = 12.5.sp,
                        color = TextPrimary
                    )
                } else {
                    Text(
                        text = "Substitusi x = $x  →  ${soal.a}($x) + ${soal.b} = $kiri",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (kiri > kanan) "Ruas kiri terlalu berat, geser slider x ke kiri." else "Ruas kanan lebih berat, geser slider x ke kanan.",
                        fontSize = 12.5.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Nilai Variabel x", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TextPrimary)
                    Text("x = $x", fontWeight = FontWeight.ExtraBold, fontSize = 14.5.sp, color = mathAmber)
                }
                Slider(
                    value = tebakan,
                    onValueChange = { tebakan = it },
                    valueRange = 0f..15f,
                    steps = 14,
                    colors = SliderDefaults.colors(thumbColor = mathAmber, activeTrackColor = mathAmber)
                )

                Button(
                    onClick = {
                        soal = buatSoal()
                        tebakan = 0f
                        sudahDihitungSelesai = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = mathAmber),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text(" Tantangan Persamaan Baru ", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
