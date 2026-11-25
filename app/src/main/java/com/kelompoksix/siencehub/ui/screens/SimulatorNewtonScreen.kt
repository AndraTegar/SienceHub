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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.components.LabMetricItem
import com.kelompoksix.siencehub.ui.components.LabMetricRow
import com.kelompoksix.siencehub.ui.components.LabSensorSwitchCard
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.StatusDanger
import com.kelompoksix.siencehub.ui.theme.SurfaceMuted
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import kotlin.math.abs

@Composable
fun SimulatorNewtonScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()
    val physicsBlue = Color(0xFF2563EB)
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

    EksperimenScaffold(
        judul = "HUKUM II NEWTON",
        subjudul = "Hubungan Gaya, Massa, & Percepatan",
        sensorBadge = "ACCEL",
        headerColor = physicsBlue,
        onBackClick = onBackClick
    ) {
        LabSensorSwitchCard(
            title = "Sensor Accelerometer",
            activeSubtitle = "Miringkan HP ke kiri atau kanan untuk memberi gaya dorong (F)",
            manualSubtitle = "Mode manual aktif — atur besar gaya dorong lewat slider",
            sensorEnabled = modeSensor,
            sensorAvailable = sensorTersedia,
            onSensorToggle = {
                modeSensor = it
                reset()
            },
            accentColor = physicsBlue
        )

        LabMetricRow(
            items = listOf(
                LabMetricItem(
                    label = "Gaya (F)",
                    value = "${"%.1f".format(gaya)} N",
                    highlightColor = StatusDanger
                ),
                LabMetricItem(
                    label = "Percepatan (a)",
                    value = "${"%.2f".format(a)} m/s²",
                    highlightColor = physicsBlue
                ),
                LabMetricItem(
                    label = "Kecepatan (v)",
                    value = "${"%.1f".format(kecepatan)} m/s"
                )
            )
        )

        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceMuted),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lintasan Gesek Rendah (0 – 20 m)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Posisi: ${"%.1f".format(posisi)} m",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = physicsBlue
                    )
                }

                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(if (metrics.isShortScreen) 155.dp else 180.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    val lantai = size.height - 26.dp.toPx()

                    // Garis lintasan & penanda jarak tiap 5m
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(0f, lantai),
                        end = Offset(size.width, lantai),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    for (tick in 0..4) {
                        val tx = (tick / 4f) * size.width
                        drawLine(
                            color = Color(0xFF94A3B8),
                            start = Offset(tx, lantai),
                            end = Offset(tx, lantai + 8.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    // Balok bermassa m
                    val sisi = (32f + massa * 1.9f).dp.toPx()
                    val x = (posisi / panjangLintasan) * (size.width - sisi)
                    drawRoundRect(
                        color = physicsBlue,
                        topLeft = Offset(x, lantai - sisi),
                        size = Size(sisi, sisi),
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )

                    // Vektor panah gaya F
                    if (abs(gaya) >= 1f) {
                        val yPanah = lantai - sisi - 16.dp.toPx()
                        val panjangPanah = (18f + abs(gaya) * 1.05f).dp.toPx()
                        val tebal = 4.dp.toPx()
                        val tengahBalok = x + sisi / 2f

                        if (gaya > 0f) {
                            val ujung = (tengahBalok + panjangPanah).coerceAtMost(size.width)
                            drawLine(StatusDanger, Offset(tengahBalok, yPanah), Offset(ujung, yPanah), tebal, cap = StrokeCap.Round)
                            drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah - 6.dp.toPx()), tebal, cap = StrokeCap.Round)
                            drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah + 6.dp.toPx()), tebal, cap = StrokeCap.Round)
                        } else {
                            val ujung = (tengahBalok - panjangPanah).coerceAtLeast(0f)
                            drawLine(StatusDanger, Offset(tengahBalok, yPanah), Offset(ujung, yPanah), tebal, cap = StrokeCap.Round)
                            drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung + 10.dp.toPx(), yPanah - 6.dp.toPx()), tebal, cap = StrokeCap.Round)
                            drawLine(StatusDanger, Offset(ujung, yPanah), Offset(ujung + 10.dp.toPx(), yPanah + 6.dp.toPx()), tebal, cap = StrokeCap.Round)
                        }
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = physicsBlue.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Rumus: a = F / m  →  ${"%.1f".format(gaya)} N / ${"%.0f".format(massa)} kg = ${"%.2f".format(a)} m/s²",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = physicsBlue,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!modeSensor || !sensorTersedia) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Gaya Dorong (F)", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TextPrimary)
                        Text("${"%.0f".format(gaya)} N", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = physicsBlue)
                    }
                    Slider(
                        value = gaya.coerceIn(1f, 100f),
                        onValueChange = { gaya = it; reset() },
                        valueRange = 1f..100f,
                        enabled = !jalan,
                        colors = SliderDefaults.colors(thumbColor = physicsBlue, activeTrackColor = physicsBlue)
                    )
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Massa Balok (m)", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TextPrimary)
                    Text("${"%.0f".format(massa)} kg", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = physicsBlue)
                }
                Slider(
                    value = massa,
                    onValueChange = { massa = it; if (!modeSensor) reset() },
                    valueRange = 1f..20f,
                    enabled = modeSensor || !jalan,
                    colors = SliderDefaults.colors(thumbColor = physicsBlue, activeTrackColor = physicsBlue)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (!modeSensor || !sensorTersedia) {
                        Button(
                            onClick = { if (posisi >= panjangLintasan) reset(); jalan = !jalan },
                            colors = ButtonDefaults.buttonColors(containerColor = physicsBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) { Text(if (jalan) "Jeda" else "Dorong Balok", fontWeight = FontWeight.Bold) }
                    }
                    OutlinedButton(
                        onClick = { reset() },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.weight(1f)
                    ) { Text("Reset Posisi", color = TextPrimary, fontWeight = FontWeight.SemiBold) }
                }
            }
        }
    }
}
