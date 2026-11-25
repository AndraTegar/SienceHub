package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.theme.AccentBlue
import com.kelompoksix.siencehub.ui.theme.AccentBlueSoft
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.StatusSuccess
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

private data class SoalLinear(val a: Int, val b: Int, val c: Int)

private fun buatSoal(): SoalLinear {
    val x = Random.nextInt(1, 11)
    val a = Random.nextInt(2, 6)
    val b = Random.nextInt(1, 10)
    return SoalLinear(a, b, a * x + b)
}

@Composable
fun TimbanganScreen(onBackClick: () -> Unit) {
    var soal by remember { mutableStateOf(buatSoal()) }
    var tebakan by remember { mutableFloatStateOf(0f) }

    val x = tebakan.roundToInt()
    val kiri = soal.a * x + soal.b
    val kanan = soal.c
    val seimbang = kiri == kanan

    EksperimenScaffold("TIMBANGAN PERSAMAAN", onBackClick) {
        Text("Cari nilai x agar timbangan seimbang:", fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Text(
            text = "${soal.a}x + ${soal.b} = ${soal.c}",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentBlue
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF4FB)),
            border = BorderStroke(1.dp, AccentBlueSoft)
        ) {
            Canvas(Modifier.fillMaxWidth().height(190.dp)) {
                val cx = size.width / 2
                val cy = 60.dp.toPx()
                val panjang = size.width * 0.36f
                val sudut = ((kanan - kiri).coerceIn(-30, 30) / 30f) * 15f
                val rad = Math.toRadians(sudut.toDouble())
                val dx = (panjang * cos(rad)).toFloat()
                val dy = (panjang * sin(rad)).toFloat()
                val ujungKiri = Offset(cx - dx, cy - dy)
                val ujungKanan = Offset(cx + dx, cy + dy)
                val dasar = size.height - 10.dp.toPx()

                drawLine(Color(0xFF475569), Offset(cx, cy), Offset(cx, dasar), 6.dp.toPx())
                drawLine(Color(0xFF475569), Offset(cx - 50.dp.toPx(), dasar), Offset(cx + 50.dp.toPx(), dasar), 6.dp.toPx())
                drawLine(SagePrimary, ujungKiri, ujungKanan, 8.dp.toPx())
                listOf(ujungKiri, ujungKanan).forEach { p ->
                    drawLine(Color(0xFF94A3B8), p, Offset(p.x, p.y + 40.dp.toPx()), 2.dp.toPx())
                    drawRoundRect(
                        if (seimbang) Color(0xFF4ADE80) else Color(0xFFF87171),
                        Offset(p.x - 38.dp.toPx(), p.y + 40.dp.toPx()),
                        Size(76.dp.toPx(), 10.dp.toPx()),
                        CornerRadius(5.dp.toPx())
                    )
                }
                drawCircle(Color(0xFF334155), 8.dp.toPx(), Offset(cx, cy))
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AccentBlueSoft)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Sisi Kiri : ${soal.a}($x) + ${soal.b} = $kiri", fontSize = 14.5.sp, color = TextPrimary)
                Text("Sisi Kanan : ${soal.c}", fontSize = 14.5.sp, color = TextPrimary)
                if (seimbang) {
                    Text(
                        "Seimbang! Nilai x = $x",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess
                    )
                    Text(
                        "${soal.a}x + ${soal.b} = ${soal.c}\n${soal.a}x = ${soal.c - soal.b}\nx = ${(soal.c - soal.b) / soal.a}",
                        fontSize = 13.5.sp,
                        color = TextSecondary
                    )
                } else {
                    Text(
                        if (kiri > kanan) "Sisi kiri lebih berat, kecilkan nilai x." else "Sisi kanan lebih berat, besarkan nilai x.",
                        fontSize = 13.5.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tebakan x = $x", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Slider(
                    value = tebakan,
                    onValueChange = { tebakan = it },
                    valueRange = 0f..15f,
                    steps = 14,
                    colors = SliderDefaults.colors(thumbColor = SagePrimary, activeTrackColor = SagePrimary)
                )

                Button(
                    onClick = { soal = buatSoal(); tebakan = 0f },
                    colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Soal Baru") }
            }
        }
    }
}
