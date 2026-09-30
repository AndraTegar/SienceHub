package com.kelompoksix.siencehub.ui.screens

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
        Text("Cari x agar timbangan seimbang:", fontWeight = FontWeight.SemiBold)
        Text("${soal.a}x + ${soal.b} = ${soal.c}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E3A8A))

        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEF7))) {
            Canvas(Modifier.fillMaxWidth().height(190.dp)) {
                val cx = size.width / 2
                val cy = 60.dp.toPx()
                val panjang = size.width * 0.38f
                val sudut = ((kanan - kiri).coerceIn(-30, 30) / 30f) * 15f
                val rad = Math.toRadians(sudut.toDouble())
                val dx = (panjang * cos(rad)).toFloat()
                val dy = (panjang * sin(rad)).toFloat()
                val ujungKiri = Offset(cx - dx, cy - dy)
                val ujungKanan = Offset(cx + dx, cy + dy)
                val dasar = size.height - 8.dp.toPx()

                drawLine(Color.DarkGray, Offset(cx, cy), Offset(cx, dasar), 6.dp.toPx())
                drawLine(Color.DarkGray, Offset(cx - 50.dp.toPx(), dasar), Offset(cx + 50.dp.toPx(), dasar), 6.dp.toPx())
                drawLine(Color(0xFF7D9078), ujungKiri, ujungKanan, 8.dp.toPx())
                listOf(ujungKiri, ujungKanan).forEach { p ->
                    drawLine(Color.Gray, p, Offset(p.x, p.y + 40.dp.toPx()), 2.dp.toPx())
                    drawRoundRect(
                        if (seimbang) Color(0xFF6FCF97) else Color(0xFFFF7F7F),
                        Offset(p.x - 40.dp.toPx(), p.y + 40.dp.toPx()),
                        Size(80.dp.toPx(), 10.dp.toPx()), CornerRadius(5.dp.toPx())
                    )
                }
                drawCircle(Color.DarkGray, 8.dp.toPx(), Offset(cx, cy))
            }
        }

        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFC9DDF5))) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Kiri : ${soal.a}(${x}) + ${soal.b} = $kiri", fontSize = 15.sp, color = Color.DarkGray)
                Text("Kanan : ${soal.c}", fontSize = 15.sp, color = Color.DarkGray)
                if (seimbang) {
                    Text("Seimbang! x = $x", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Text("${soal.a}x + ${soal.b} = ${soal.c}\n${soal.a}x = ${soal.c - soal.b}\nx = ${(soal.c - soal.b) / soal.a}", fontSize = 14.sp, color = Color.DarkGray)
                } else {
                    Text(if (kiri > kanan) "Sisi kiri lebih berat, kecilkan x." else "Sisi kanan lebih berat, besarkan x.", fontSize = 14.sp, color = Color.DarkGray)
                }
            }
        }

        Text("Tebakan x = $x", fontWeight = FontWeight.SemiBold)
        Slider(value = tebakan, onValueChange = { tebakan = it }, valueRange = 0f..15f, steps = 14)

        Button(
            onClick = { soal = buatSoal(); tebakan = 0f },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D9078))
        ) { Text("Soal baru") }
    }
}