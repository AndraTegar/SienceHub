package com.kelompoksix.siencehub.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun SimulatorNewtonScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val kuning = Color(0xFFFFC94D)
    val hijau = Color(0xFF7D9078)
    val panjangLintasan = 20f // meter

    var modeSensor by remember { mutableStateOf(true) }
    var sensorTersedia by remember { mutableStateOf(true) }

    var gaya by remember { mutableFloatStateOf(20f) }        // N (bisa negatif/positif di mode sensor)
    var massa by remember { mutableFloatStateOf(5f) }        // kg
    var jalan by remember { mutableStateOf(false) }
    var posisi by remember { mutableFloatStateOf(panjangLintasan / 2f) } // mulai dari tengah saat mode sensor
    var kecepatan by remember { mutableFloatStateOf(0f) }

    // Percepatan a = F / m
    val a = gaya / massa

    // ---------- Listener Sensor Accelerometer ----------
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
                        // Sumbu X: miring kanan = negatif, miring kiri = positif
                        // Dibalik agar miring kanan -> gaya positif (gerak ke kanan)
                        val tiltX = -event.values[0]

                        // Deadzone agar balok tidak bergetar saat HP datar
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

    // ---------- Loop Fisika ----------
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

            // Sedikit redaman gesekan udara di mode sensor agar balok lebih enak dikontrol
            if (modeSensor && gaya == 0f) {
                kecepatan *= 0.96f
                if (abs(kecepatan) < 0.05f) kecepatan = 0f
            }

            posisi += kecepatan * dt

            // Batas lintasan kiri (0) dan kanan (panjangLintasan)
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
                // ---------- Toggle Mode Sensor ----------
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Mode Sensor Accelerometer", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                if (modeSensor) "Miringkan HP ke kiri/kanan untuk memberi gaya"
                                else "Atur gaya secara manual dengan slider",
                                fontSize = 12.sp, color = Color.Gray
                            )
                        }
                        Switch(
                            checked = modeSensor,
                            onCheckedChange = {
                                modeSensor = it
                                reset()
                            },
                            colors = SwitchDefaults.colors(checkedTrackColor = hijau)
                        )
                    }
                }

                if (modeSensor && !sensorTersedia) {
                    Text(
                        "Sensor accelerometer tidak ditemukan di perangkat ini.",
                        color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.SemiBold
                    )
                }

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

                        // Panah gaya (dinamis ke kiri atau ke kanan)
                        if (abs(gaya) >= 1f) {
                            val yPanah = lantai - sisi - 16.dp.toPx()
                            val panjangPanah = (16f + abs(gaya) * 1.1f).dp.toPx()
                            val tebal = 4.dp.toPx()
                            val tengahBalok = x + sisi / 2f

                            if (gaya > 0f) {
                                // Panah ke kanan
                                val ujung = (tengahBalok + panjangPanah).coerceAtMost(size.width)
                                drawLine(Color.Red, Offset(tengahBalok, yPanah), Offset(ujung, yPanah), tebal)
                                drawLine(Color.Red, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah - 6.dp.toPx()), tebal)
                                drawLine(Color.Red, Offset(ujung, yPanah), Offset(ujung - 10.dp.toPx(), yPanah + 6.dp.toPx()), tebal)
                            } else {
                                // Panah ke kiri
                                val ujung = (tengahBalok - panjangPanah).coerceAtLeast(0f)
                                drawLine(Color.Red, Offset(tengahBalok, yPanah), Offset(ujung, yPanah), tebal)
                                drawLine(Color.Red, Offset(ujung, yPanah), Offset(ujung + 10.dp.toPx(), yPanah - 6.dp.toPx()), tebal)
                                drawLine(Color.Red, Offset(ujung, yPanah), Offset(ujung + 10.dp.toPx(), yPanah + 6.dp.toPx()), tebal)
                            }
                        }
                    }
                }

                // ---------- Hasil ----------
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFC9DDF5))
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "a = F / m = ${"%.1f".format(gaya)} / ${"%.0f".format(massa)} = ${"%.1f".format(a)} m/s²",
                            fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A)
                        )
                        Text("Kecepatan: ${"%.1f".format(kecepatan)} m/s", fontSize = 14.sp, color = Color.DarkGray)
                        Text("Posisi: ${"%.1f".format(posisi)} m / ${panjangLintasan.toInt()} m", fontSize = 14.sp, color = Color.DarkGray)
                    }
                }

                // ---------- Kontrol ----------
                if (!modeSensor) {
                    Text("Gaya (F): ${"%.0f".format(gaya)} N", fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = gaya.coerceIn(1f, 100f),
                        onValueChange = { gaya = it; reset() },
                        valueRange = 1f..100f,
                        enabled = !jalan
                    )
                } else {
                    Text("Gaya dari Kemiringan (F): ${"%.1f".format(gaya)} N", fontWeight = FontWeight.SemiBold)
                }

                Text("Massa (m): ${"%.0f".format(massa)} kg", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = massa,
                    onValueChange = { massa = it; if (!modeSensor) reset() },
                    valueRange = 1f..20f,
                    enabled = modeSensor || !jalan
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!modeSensor) {
                        Button(
                            onClick = { if (posisi >= panjangLintasan) reset(); jalan = !jalan },
                            colors = ButtonDefaults.buttonColors(containerColor = hijau)
                        ) { Text(if (jalan) "Jeda" else "Jalankan") }
                    }
                    OutlinedButton(onClick = { reset() }) { Text("Reset Posisi") }
                }

                Text(
                    "Coba: miringkan HP sambil mengubah massa balok. Semakin besar massa, semakin lambat percepatannya!",
                    fontSize = 13.sp, color = Color.DarkGray
                )
            }
        }
    }
}