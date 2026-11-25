package com.kelompoksix.siencehub.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.EksperimenScaffold
import com.kelompoksix.siencehub.ui.components.LabMetricItem
import com.kelompoksix.siencehub.ui.components.LabMetricRow
import com.kelompoksix.siencehub.ui.components.LabSensorSwitchCard
import com.kelompoksix.siencehub.ui.components.PilihanChip
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import kotlin.math.roundToInt
import kotlin.random.Random

private data class GelembungO2(
    val id: Int,
    var xFraction: Float, // -1f..1f di sekitar corong tengah
    var progressY: Float, // 0f (daun bawah) -> 1f (puncak tabung reaksi)
    val radiusDp: Float,
    val wobblePhase: Float
)

private enum class KadarCo2(val label: String, val faktor: Float, val keterangan: String) {
    RENDAH("Minim CO₂ (Air Matang)", 0.45f, "Air dididihkan miskin CO₂ terlarut → fotosintesis terhambat"),
    NORMAL("Air Keran Normal", 1.0f, "Kadar CO₂ terlarut standar untuk fotosintesis normal"),
    OPTIMAL("+ NaHCO₃ (Soda Kue)", 1.45f, "Penambahan NaHCO₃ memperkaya CO₂ terlarut → laju O₂ meningkat drastis")
}

@Composable
fun FotosintesisScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()
    val bioGreen = Color(0xFF15803D)

    var modeSensor by remember { mutableStateOf(true) }
    var sensorTersedia by remember { mutableStateOf(true) }

    var sensorLux by remember { mutableFloatStateOf(250f) }
    var manualLux by remember { mutableFloatStateOf(450f) }
    var kadarCo2 by remember { mutableStateOf(KadarCo2.NORMAL) }
    var totalGelembungTerkumpul by remember { mutableIntStateOf(0) }

    // Integrasi Sensor Fisik ke-2: Sensor.TYPE_LIGHT (Lux Meter Real-Time)
    DisposableEffect(modeSensor) {
        if (!modeSensor) {
            onDispose { }
        } else {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

            if (lightSensor == null) {
                sensorTersedia = false
                onDispose { }
            } else {
                sensorTersedia = true
                val listener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent) {
                        val bacaanLux = event.values.firstOrNull()?.coerceAtLeast(0f) ?: 0f
                        sensorLux = bacaanLux
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                }
                sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_UI)
                onDispose {
                    sensorManager.unregisterListener(listener)
                }
            }
        }
    }

    val luxAktif = if (modeSensor && sensorTersedia) sensorLux else manualLux

    // Kurva kejenuhan cahaya Michaelis-Menten: v = Vmax * [Lux / (Km + Lux)]
    // Km = 450 lux (setengah laju maksimum), dikalikan faktor ketersediaan CO2
    val efisiensiCahaya = (luxAktif / (450f + luxAktif)).coerceIn(0f, 1f)
    val lajuGelembungPerMenit = (efisiensiCahaya * 90f * kadarCo2.faktor).coerceAtLeast(0f)
    val persentaseLaju = ((lajuGelembungPerMenit / (90f * KadarCo2.OPTIMAL.faktor)) * 100f).coerceIn(0f, 100f)

    val kategoriCahaya = when {
        luxAktif < 20f -> "Gelap (Respirasi Dominan)"
        luxAktif < 150f -> "Redup / Teduh"
        luxAktif < 800f -> "Terang Ruangan"
        luxAktif < 2200f -> "Cahaya Optimal"
        else -> "Titik Jenuh Cahaya"
    }

    val gelembungList = remember { mutableStateListOf<GelembungO2>() }

    // Loop animasi gelembung O2 naik di corong Ingenhousz sesuai laju fotosintesis real-time
    LaunchedEffect(Unit) {
        var lastNanos = withFrameNanos { it }
        var spawnAccumulator = 0f
        var nextBubbleId = 1

        while (true) {
            val now = withFrameNanos { it }
            val dt = ((now - lastNanos) / 1_000_000_000f).coerceIn(0f, 0.05f)
            lastNanos = now

            val currentLux = if (modeSensor && sensorTersedia) sensorLux else manualLux
            val currentEff = (currentLux / (450f + currentLux)).coerceIn(0f, 1f)
            val bubblesPerSec = (currentEff * 90f * kadarCo2.faktor) / 60f

            spawnAccumulator += bubblesPerSec * dt
            while (spawnAccumulator >= 1f && gelembungList.size < 36) {
                spawnAccumulator -= 1f
                gelembungList.add(
                    GelembungO2(
                        id = nextBubbleId++,
                        xFraction = Random.nextFloat() * 1.4f - 0.7f,
                        progressY = 0f,
                        radiusDp = Random.nextFloat() * 2.2f + 2.6f,
                        wobblePhase = Random.nextFloat() * 6.28f
                    )
                )
            }

            val riseSpeed = 0.36f + currentEff * 0.45f
            val iterator = gelembungList.listIterator()
            while (iterator.hasNext()) {
                val b = iterator.next()
                val nextY = b.progressY + riseSpeed * dt
                if (nextY >= 1f) {
                    iterator.remove()
                    totalGelembungTerkumpul++
                } else {
                    iterator.set(b.copy(progressY = nextY))
                }
            }
        }
    }

    EksperimenScaffold(
        judul = "LAJU FOTOSINTESIS",
        subjudul = "Percobaan Ingenhousz • Tanaman Hydrilla",
        sensorBadge = "LUX SENSOR",
        headerColor = bioGreen,
        onBackClick = onBackClick
    ) {
        // 1. Kartu Sakelar Sensor Cahaya HP
        LabSensorSwitchCard(
            title = "Sensor Cahaya (TYPE_LIGHT)",
            activeSubtitle = "Tutup bagian atas HP dengan tangan atau arahkan ke lampu/senter untuk mengubah Lux",
            manualSubtitle = "Mode simulasi manual aktif — atur intensitas cahaya lewat slider di bawah",
            sensorEnabled = modeSensor,
            sensorAvailable = sensorTersedia,
            onSensorToggle = { modeSensor = it },
            accentColor = bioGreen
        )

        // 2. Baris Indikator Real-Time Responsif
        LabMetricRow(
            items = listOf(
                LabMetricItem(
                    label = "Intensitas Cahaya",
                    value = "${luxAktif.roundToInt()} lx",
                    highlightColor = Color(0xFFD97706)
                ),
                LabMetricItem(
                    label = "Laju Gelembung O₂",
                    value = "${lajuGelembungPerMenit.roundToInt()}/mnt",
                    highlightColor = bioGreen
                ),
                LabMetricItem(
                    label = "O₂ Terkumpul",
                    value = "$totalGelembungTerkumpul blb",
                    highlightColor = Color(0xFF0288D1)
                )
            )
        )

        // 3. Visualisasi Tabung Percobaan Ingenhousz Real-Time
        Card(
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Status bar kondisi cahaya di atas kanvas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = kategoriCahaya,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    TextButton(
                        onClick = {
                            totalGelembungTerkumpul = 0
                            gelembungList.clear()
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = bioGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Reset O₂", fontSize = 11.5.sp, color = bioGreen, fontWeight = FontWeight.Bold)
                    }
                }

                val brightnessRatio = (luxAktif / 1600f).coerceIn(0.05f, 1f)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (metrics.isShortScreen) 195.dp else 225.dp)
                ) {
                    // Latar belakang langit/ruang lab berubah terang-gelap mengikuti bacaan Lux sensor!
                    val topSky = Color(
                        red = 0.07f + 0.72f * brightnessRatio,
                        green = 0.11f + 0.78f * brightnessRatio,
                        blue = 0.18f + 0.78f * brightnessRatio
                    )
                    val bottomSky = Color(
                        red = 0.05f + 0.55f * brightnessRatio,
                        green = 0.10f + 0.68f * brightnessRatio,
                        blue = 0.15f + 0.70f * brightnessRatio
                    )
                    drawRect(brush = Brush.verticalGradient(listOf(topSky, bottomSky)))

                    // Efek pendar cahaya matahari/lampu di pojok kanan atas
                    val sunCenter = Offset(size.width * 0.84f, size.height * 0.20f)
                    val sunRadius = (16f + 22f * brightnessRatio).dp.toPx()
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFDE047).copy(alpha = 0.25f + 0.65f * brightnessRatio),
                                Color(0xFFF59E0B).copy(alpha = 0.15f * brightnessRatio),
                                Color.Transparent
                            ),
                            center = sunCenter,
                            radius = sunRadius * 2.4f
                        ),
                        radius = sunRadius * 2.4f,
                        center = sunCenter
                    )
                    drawCircle(
                        color = Color(0xFFFACC15).copy(alpha = 0.35f + 0.65f * brightnessRatio),
                        radius = sunRadius * 0.55f,
                        center = sunCenter
                    )

                    // Gelas Kimia (Beaker) di tengah
                    val cx = size.width * 0.48f
                    val beakerW = (size.width * 0.46f).coerceIn(130.dp.toPx(), 210.dp.toPx())
                    val beakerH = size.height * 0.76f
                    val beakerLeft = cx - beakerW / 2f
                    val beakerTop = size.height * 0.16f
                    val beakerBottom = beakerTop + beakerH

                    // Air di dalam gelas kimia
                    val waterTop = beakerTop + beakerH * 0.16f
                    drawRoundRect(
                        color = Color(0xFF38BDF8).copy(alpha = 0.32f + 0.18f * brightnessRatio),
                        topLeft = Offset(beakerLeft + 4.dp.toPx(), waterTop),
                        size = Size(beakerW - 8.dp.toPx(), beakerBottom - waterTop - 3.dp.toPx()),
                        cornerRadius = CornerRadius(12.dp.toPx())
                    )

                    // Tanaman air Hydrilla di dasar corong
                    val plantBaseY = beakerBottom - 8.dp.toPx()
                    val stemOffsets = listOf(-22.dp.toPx(), -8.dp.toPx(), 8.dp.toPx(), 22.dp.toPx())
                    stemOffsets.forEachIndexed { idx, dx ->
                        val stemHeight = (34 + (idx % 2) * 10).dp.toPx()
                        val sx = cx + dx
                        drawLine(
                            color = Color(0xFF15803D),
                            start = Offset(sx, plantBaseY),
                            end = Offset(sx + dx * 0.15f, plantBaseY - stemHeight),
                            strokeWidth = 3.2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        // Daun-daun kecil Hydrilla
                        for (leafIdx in 1..4) {
                            val ly = plantBaseY - (stemHeight * leafIdx / 4.5f)
                            drawLine(
                                color = Color(0xFF22C55E),
                                start = Offset(sx, ly),
                                end = Offset(sx - 8.dp.toPx(), ly - 4.dp.toPx()),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = Color(0xFF22C55E),
                                start = Offset(sx, ly),
                                end = Offset(sx + 8.dp.toPx(), ly - 4.dp.toPx()),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    // Corong kaca terbalik + tabung reaksi penampung gas O2
                    val funnelWideW = beakerW * 0.66f
                    val neckW = 24.dp.toPx()
                    val funnelBottomY = beakerBottom - 6.dp.toPx()
                    val funnelMidY = beakerBottom - beakerH * 0.40f
                    val tubeTopY = beakerTop + 4.dp.toPx()

                    val funnelPath = Path().apply {
                        moveTo(cx - funnelWideW / 2f, funnelBottomY)
                        lineTo(cx - neckW / 2f, funnelMidY)
                        lineTo(cx - neckW / 2f, tubeTopY + 10.dp.toPx())
                        quadraticTo(cx, tubeTopY, cx + neckW / 2f, tubeTopY + 10.dp.toPx())
                        lineTo(cx + neckW / 2f, funnelMidY)
                        lineTo(cx + funnelWideW / 2f, funnelBottomY)
                    }
                    drawPath(
                        path = funnelPath,
                        color = Color.White.copy(alpha = 0.68f),
                        style = Stroke(width = 2.2.dp.toPx())
                    )

                    // Ruang gas O2 yang terkumpul di puncak tabung reaksi
                    val gasHeight = ((totalGelembungTerkumpul.coerceAtMost(80) / 80f) * 28.dp.toPx())
                    if (gasHeight > 1f) {
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.72f),
                            topLeft = Offset(cx - neckW / 2f + 2.dp.toPx(), tubeTopY + 6.dp.toPx()),
                            size = Size(neckW - 4.dp.toPx(), gasHeight),
                            cornerRadius = CornerRadius(6.dp.toPx())
                        )
                    }

                    // Gelembung-gelembung O2 yang bergerak naik ke puncak tabung reaksi
                    val startBubbleY = funnelBottomY - 22.dp.toPx()
                    val endBubbleY = tubeTopY + 12.dp.toPx() + gasHeight
                    gelembungList.forEach { b ->
                        val currentY = startBubbleY + (endBubbleY - startBubbleY) * b.progressY
                        // Saat masih di kerucut bawah agak lebar, saat masuk leher tabung menyempit ke tengah
                        val spreadFactor = (1f - (b.progressY * 1.4f).coerceAtMost(0.85f))
                        val maxHalfWidth = (funnelWideW * 0.32f * spreadFactor).coerceAtLeast(neckW * 0.25f)
                        val bx = cx + b.xFraction * maxHalfWidth

                        drawCircle(
                            color = Color.White.copy(alpha = 0.90f),
                            radius = b.radiusDp.dp.toPx(),
                            center = Offset(bx, currentY)
                        )
                        drawCircle(
                            color = Color(0xFF0284C7).copy(alpha = 0.55f),
                            radius = b.radiusDp.dp.toPx(),
                            center = Offset(bx, currentY),
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }

                    // Garis dinding luar Gelas Kimia
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.85f),
                        topLeft = Offset(beakerLeft, beakerTop),
                        size = Size(beakerW, beakerH),
                        cornerRadius = CornerRadius(14.dp.toPx()),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }

        // 4. Kurva Kejenuhan Cahaya Real-Time + Reaksi Fotosintesis
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kurva Laju Fotosintesis vs Cahaya",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Aktivitas ${persentaseLaju.roundToInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = bioGreen
                    )
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                ) {
                    val padLeft = 8.dp.toPx()
                    val padBottom = 10.dp.toPx()
                    val plotW = size.width - padLeft * 2
                    val plotH = size.height - padBottom - 8.dp.toPx()
                    val maxGraphLux = 3000f

                    // Garis sumbu X & Y
                    drawLine(
                        color = Color(0xFF94A3B8).copy(alpha = 0.5f),
                        start = Offset(padLeft, 6.dp.toPx()),
                        end = Offset(padLeft, 6.dp.toPx() + plotH),
                        strokeWidth = 1.5.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF94A3B8).copy(alpha = 0.5f),
                        start = Offset(padLeft, 6.dp.toPx() + plotH),
                        end = Offset(padLeft + plotW, 6.dp.toPx() + plotH),
                        strokeWidth = 1.5.dp.toPx()
                    )

                    // Gambar kurva Michaelis-Menten sesuai kadar CO2 aktif
                    val curvePath = Path()
                    val steps = 40
                    val scaleY = kadarCo2.faktor / KadarCo2.OPTIMAL.faktor
                    for (s in 0..steps) {
                        val lx = (s.toFloat() / steps) * maxGraphLux
                        val eff = (lx / (450f + lx)) * scaleY
                        val px = padLeft + (lx / maxGraphLux) * plotW
                        val py = 6.dp.toPx() + plotH * (1f - eff)
                        if (s == 0) curvePath.moveTo(px, py) else curvePath.lineTo(px, py)
                    }
                    drawPath(
                        path = curvePath,
                        color = bioGreen,
                        style = Stroke(width = 2.6.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Titik posisi Lux saat ini pada kurva
                    val clampedLux = luxAktif.coerceIn(0f, maxGraphLux)
                    val dotEff = (clampedLux / (450f + clampedLux)) * scaleY
                    val dotX = padLeft + (clampedLux / maxGraphLux) * plotW
                    val dotY = 6.dp.toPx() + plotH * (1f - dotEff)

                    drawCircle(
                        color = Color(0xFFF59E0B).copy(alpha = 0.28f),
                        radius = 10.dp.toPx(),
                        center = Offset(dotX, dotY)
                    )
                    drawCircle(
                        color = Color(0xFFF59E0B),
                        radius = 5.5.dp.toPx(),
                        center = Offset(dotX, dotY)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = bioGreen.copy(alpha = 0.10f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "6CO₂ + 6H₂O  ──(Cahaya + Klorofil)──▶  C₆H₁₂O₆ + 6O₂↑",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = bioGreen,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
                    )
                }
            }
        }

        // 5. Panel Kontrol Faktor Pembatas (Kadar CO2 + Slider Cahaya jika Mode Manual)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!modeSensor || !sensorTersedia) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Intensitas Cahaya Manual",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "${manualLux.roundToInt()} Lux",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = bioGreen
                        )
                    }
                    Slider(
                        value = manualLux,
                        onValueChange = { manualLux = it },
                        valueRange = 0f..3000f,
                        colors = SliderDefaults.colors(
                            thumbColor = bioGreen,
                            activeTrackColor = bioGreen
                        )
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "Gelap (5 lx)" to 5f,
                            "Redup (80 lx)" to 80f,
                            "Ruang (450 lx)" to 450f,
                            "Terang (1500 lx)" to 1500f,
                            "Terik (2800 lx)" to 2800f
                        ).forEach { (label, targetLux) ->
                            PilihanChip(
                                teks = label,
                                terpilih = kotlin.math.abs(manualLux - targetLux) < 35f,
                                accentColor = bioGreen
                            ) {
                                manualLux = targetLux
                            }
                        }
                    }
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 4.dp))
                }

                Text(
                    text = "Variabel Kondisi Air (Kadar CO₂):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = TextPrimary
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KadarCo2.entries.forEach { opsi ->
                        PilihanChip(
                            teks = opsi.label,
                            terpilih = kadarCo2 == opsi,
                            accentColor = bioGreen
                        ) {
                            kadarCo2 = opsi
                        }
                    }
                }
                Text(
                    text = kadarCo2.keterangan,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
