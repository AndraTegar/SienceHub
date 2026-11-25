package com.kelompoksix.siencehub.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.theme.AccentBlue
import com.kelompoksix.siencehub.ui.theme.AccentBlueSoft
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.StatusDanger
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import kotlin.math.abs

@Composable
fun SimulatorNewtonScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val panjangLintasan = 20f

    var modeSensor by remember { mutableStateOf(true) }
    var sensorTersedia by remember { mutableStateOf(true) }

    var gaya by remember { mutableFloatStateOf(20f) }
    var massa by remember { mutableFloatStateOf(5f) }
    var jalan by remember { mutableStateOf(false) }
    var posisi by remember { mutableFloatStateOf(panjangLintasan / 2f) }
    var kecepatan by remember { mutableFloatStateOf(0f) }

    val a = gaya / massa

    DisposableEffect(modeSensor) {
        if (!modeSensor) {
            onDispose { }
        } else {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            if (accelerometer == null) {
                sensorTersedia = false
                onDispose { }
            } else {
                sensorTersedia = true
                val listener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent) {
                        val tiltX = -event.values[0]
                        gaya = if (abs(tiltX) < 0.4f) {
                            0f
                        } else {
                            (tiltX * 10f).coerceIn(-100f, 100f)
                        }
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                }

                sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
                onDispose {
                    sensorManager.unregisterListener(listener)
                }
            }
        }
    }

    val aktif = if (modeSensor) sensorTersedia else jalan

    LaunchedEffect(aktif, modeSensor) {
        if (!aktif) return@LaunchedEffect
        var terakhir = withFrameNanos { it }
        while (aktif) {
            val sekarang = withFrameNanos { it }
            val dt = ((sekarang - terakhir) / 1_000_000_000f).coerceAtMost(0.05f)
            terakhir = sekarang

            val percepatanSaatIni = gaya / massa
            kecepatan += percepatanSaatIni * dt

            if (modeSensor && gaya == 0f) {
                kecepatan *= 0.96f
                if (abs(kecepatan) < 0.05f) kecepatan = 0f
            }

            posisi += kecepatan * dt

            if (posisi >= panjangLintasan) {
                posisi = panjangLintasan
                kecepatan = 0f
                if (!modeSensor) jalan = false
            } else if (posisi <= 0f) {
                posisi = 0f
                kecepatan = 0f
            }
        }
    }

    fun reset() {
        jalan = false
        kecepatan = 0f
        posisi = if (modeSensor) panjangLintasan / 2f else 0f
        if (!modeSensor && gaya < 1f) gaya = 20f
    }

    EksperimenScaffold(judul = "HUKUM II NEWTON", onBackClick = onBackClick) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Mode Sensor Accelerometer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        if (modeSensor) "Miringkan HP ke kiri/kanan untuk memberi gaya"
                        else "Atur gaya secara manual dengan slider",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = modeSensor,
                    onCheckedChange = {
                        modeSensor = it
                        reset()
                    },
                    colors = SwitchDefaults.colors(checkedTrackColor = SagePrimary)
                )
            }
        }

        if (modeSensor && !sensorTersedia) {
            Text(
                "Sensor accelerometer tidak ditemukan di perangkat ini.",
                color = StatusDanger,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF4FB)),
            border = BorderStroke(1.dp, AccentBlueSoft)
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .padding(horizontal = 12.dp)
            ) {
                val lantai = size.height - 24.dp.toPx()
                drawLine(Color(0xFF475569), Offset(0f, lantai), Offset(size.width, lantai), 4.dp.toPx())

                val sisi = (30f + massa * 2f).dp.toPx()
                val x = (posisi / panjangLintasan) * (size.width - sisi)
                drawRect(SagePrimary, Offset(x, lantai - sisi), Size(sisi, sisi))

                if (abs(gaya) >= 1f) {
                    val yPanah = lantai - sisi - 16.dp.toPx()
                    val panjangPanah = (16f + abs(gaya) * 1.1f).dp.toPx()
                    val tebal = 4.dp.toPx()
                    val tengahBalok = x + sisi / 2f

                    if (gaya > 0f) {
                        val ujung = (tengahBalok + panjangPanah).coerceAtMost(size.width)
                        drawLine(StatusDanger, Offset(tengahBalok, yPanah), Offset(ujung, yPanah), tebal)
                        drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah - 6.dp.toPx()), tebal)
                        drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah + 6.dp.toPx()), tebal)
                    } else {
                        val ujung = (tengahBalok - panjangPanah).coerceAtLeast(0f)
                        drawLine(StatusDanger, Offset(tengahBalok, yPanah), Offset(ujung, yPanah), tebal)
                        drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung + 10.dp.toPx(), yPanah - 6.dp.toPx()), tebal)
                        drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung + 10.dp.toPx(), yPanah + 6.dp.toPx()), tebal)
                    }
                }
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AccentBlueSoft)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "a = F / m = ${"%.1f".format(gaya)} / ${"%.0f".format(massa)} = ${"%.1f".format(a)} m/s²",
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                )
                Text("Kecepatan: ${"%.1f".format(kecepatan)} m/s", fontSize = 13.5.sp, color = TextPrimary)
                Text("Posisi: ${"%.1f".format(posisi)} m / ${panjangLintasan.toInt()} m", fontSize = 13.5.sp, color = TextPrimary)
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!modeSensor) {
                    Text("Gaya (F): ${"%.0f".format(gaya)} N", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Slider(
                        value = gaya.coerceIn(1f, 100f),
                        onValueChange = { gaya = it; reset() },
                        valueRange = 1f..100f,
                        enabled = !jalan,
                        colors = SliderDefaults.colors(thumbColor = SagePrimary, activeTrackColor = SagePrimary)
                    )
                } else {
                    Text("Gaya dari Kemiringan (F): ${"%.1f".format(gaya)} N", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }

                Text("Massa (m): ${"%.0f".format(massa)} kg", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Slider(
                    value = massa,
                    onValueChange = { massa = it; if (!modeSensor) reset() },
                    valueRange = 1f..20f,
                    enabled = modeSensor || !jalan,
                    colors = SliderDefaults.colors(thumbColor = SagePrimary, activeTrackColor = SagePrimary)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!modeSensor) {
                        Button(
                            onClick = { if (posisi >= panjangLintasan) reset(); jalan = !jalan },
                            colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text(if (jalan) "Jeda" else "Jalankan") }
                    }
                    OutlinedButton(
                        onClick = { reset() },
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Reset Posisi") }
                }
            }
        }

        Text(
            "Tips: miringkan HP sambil mengubah massa balok. Semakin besar massa, semakin lambat percepatannya!",
            fontSize = 12.5.sp,
            color = TextSecondary
        )
    }
}
