package com.kelompoksix.siencehub.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
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
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

private data class GelembungO2(
    val id: Int,
    var xFraction: Float, // -1f..1f di sekitar corong tengah
    var progressY: Float, // 0f (daun bawah) -> 1f (puncak tabung reaksi)
    val radiusDp: Float,
    val wobblePhase: Float
)

private data class BintangMalam(
    val xFrac: Float,
    val yFrac: Float,
    val radiusDp: Float,
    val phase: Float,
    val isSparkle: Boolean = false
)

private val daftarBintangMalam = listOf(
    BintangMalam(0.08f, 0.14f, 1.4f, 0.2f, isSparkle = true),
    BintangMalam(0.16f, 0.28f, 1.0f, 1.5f),
    BintangMalam(0.24f, 0.11f, 1.2f, 2.8f),
    BintangMalam(0.11f, 0.46f, 0.9f, 4.1f),
    BintangMalam(0.29f, 0.36f, 1.1f, 0.9f),
    BintangMalam(0.38f, 0.12f, 1.0f, 3.4f),
    BintangMalam(0.55f, 0.10f, 1.3f, 2.1f, isSparkle = true),
    BintangMalam(0.64f, 0.22f, 1.0f, 5.0f),
    BintangMalam(0.71f, 0.12f, 1.1f, 1.1f),
    BintangMalam(0.69f, 0.39f, 0.9f, 3.9f),
    BintangMalam(0.92f, 0.13f, 1.1f, 2.4f),
    BintangMalam(0.94f, 0.36f, 1.3f, 4.7f, isSparkle = true),
    BintangMalam(0.77f, 0.50f, 0.9f, 0.6f),
    BintangMalam(0.19f, 0.60f, 0.9f, 5.5f)
)

private fun warnaLangitBerdasarkanLux(lux: Float): Triple<Color, Color, Color> {
    // Anchor palet langit (Atas, Tengah, Ufuk Bawah) dari Malam -> Fajar/Pagi -> Siang
    val malamGelap = Triple(Color(0xFF060B19), Color(0xFF101B37), Color(0xFF1D2B4F))
    val malamAkhir = Triple(Color(0xFF0E172E), Color(0xFF1D2A4A), Color(0xFF33446B))
    val pagiTerbit = Triple(Color(0xFF284878), Color(0xFF726A95), Color(0xFFFDBA74))
    val pagiCerah = Triple(Color(0xFF2994D1), Color(0xFF6EC8F5), Color(0xFFFDE68A))
    val siangCerah = Triple(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFFBAE6FD))
    val siangTerik = Triple(Color(0xFF0369A1), Color(0xFF0EA5E9), Color(0xFFE0F2FE))

    fun campur(
        a: Triple<Color, Color, Color>,
        b: Triple<Color, Color, Color>,
        t: Float
    ): Triple<Color, Color, Color> {
        val f = t.coerceIn(0f, 1f)
        return Triple(
            lerp(a.first, b.first, f),
            lerp(a.second, b.second, f),
            lerp(a.third, b.third, f)
        )
    }

    return when {
        lux <= 45f -> campur(malamGelap, malamAkhir, lux / 45f)
        lux <= 110f -> campur(malamAkhir, pagiTerbit, (lux - 45f) / (110f - 45f))
        lux <= 350f -> campur(pagiTerbit, pagiCerah, (lux - 110f) / (350f - 110f))
        lux <= 1100f -> campur(pagiCerah, siangCerah, (lux - 350f) / (1100f - 350f))
        else -> campur(siangCerah, siangTerik, (lux - 1100f) / 1400f)
    }
}

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
    val visualLux by animateFloatAsState(
        targetValue = luxAktif,
        animationSpec = tween(durationMillis = 450),
        label = "visualLux"
    )

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
    var animTimeSec by remember { mutableFloatStateOf(0f) }

    // Loop animasi gelembung O2 naik & animasi atmosfer langit secara real-time
    LaunchedEffect(Unit) {
        var lastNanos = withFrameNanos { it }
        var spawnAccumulator = 0f
        var nextBubbleId = 1

        while (true) {
            val now = withFrameNanos { it }
            val dt = ((now - lastNanos) / 1_000_000_000f).coerceIn(0f, 0.05f)
            lastNanos = now
            animTimeSec = (animTimeSec + dt) % 3600f

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
                val ikonKondisiCahaya = when {
                    visualLux < 55f -> Icons.Default.NightsStay
                    visualLux < 300f -> Icons.Default.WbTwilight
                    else -> Icons.Default.WbSunny
                }
                val warnaIkonCahaya = when {
                    visualLux < 55f -> Color(0xFF6366F1)
                    visualLux < 300f -> Color(0xFFF97316)
                    else -> Color(0xFFF59E0B)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ikonKondisiCahaya,
                            contentDescription = null,
                            tint = warnaIkonCahaya,
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

                val brightnessRatio = (visualLux / 1600f).coerceIn(0.05f, 1f)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (metrics.isShortScreen) 195.dp else 225.dp)
                ) {
                    // 1) Gradasi langit dinamis Malam -> Pagi/Fajar -> Siang mengikuti indikator Lux
                    val (topSky, midSky, bottomSky) = warnaLangitBerdasarkanLux(visualLux)
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to topSky,
                                0.58f to midSky,
                                1.0f to bottomSky
                            )
                        )
                    )

                    val horizonY = size.height * 0.90f

                    // 2) Bintang-bintang berkelip saat malam / gelap (memudar saat fajar datang)
                    val starVisibility = (1f - (visualLux - 15f) / 65f).coerceIn(0f, 1f)
                    if (starVisibility > 0.01f) {
                        daftarBintangMalam.forEach { star ->
                            val twinkle = 0.55f + 0.45f * sin(animTimeSec * 2.4f + star.phase)
                            val alpha = (starVisibility * twinkle).coerceIn(0f, 1f)
                            val pos = Offset(size.width * star.xFrac, size.height * star.yFrac)
                            val rPx = star.radiusDp.dp.toPx()

                            drawCircle(
                                color = Color(0xFFF8FAFC).copy(alpha = alpha * 0.92f),
                                radius = rPx,
                                center = pos
                            )
                            if (star.isSparkle) {
                                val arm = rPx * 2.6f
                                drawLine(
                                    color = Color.White.copy(alpha = alpha * 0.65f),
                                    start = Offset(pos.x - arm, pos.y),
                                    end = Offset(pos.x + arm, pos.y),
                                    strokeWidth = 1.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    color = Color.White.copy(alpha = alpha * 0.65f),
                                    start = Offset(pos.x, pos.y - arm),
                                    end = Offset(pos.x, pos.y + arm),
                                    strokeWidth = 1.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    // 3) Objek BULAN SABIT saat cahaya berkurang / malam (0 .. ~85 lx)
                    val moonPresence = (1f - (visualLux - 30f) / 55f).coerceIn(0f, 1f)
                    if (moonPresence > 0.01f) {
                        val moonCenter = Offset(
                            x = size.width * 0.83f,
                            y = size.height * (0.22f + (1f - moonPresence) * 0.30f)
                        )
                        val moonRadius = 18.dp.toPx()

                        // Pendaran cahaya bulan (moonlight halo)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFBAE6FD).copy(alpha = 0.28f * moonPresence),
                                    Color(0xFF818CF8).copy(alpha = 0.10f * moonPresence),
                                    Color.Transparent
                                ),
                                center = moonCenter,
                                radius = moonRadius * 2.5f
                            ),
                            radius = moonRadius * 2.5f,
                            center = moonCenter
                        )

                        // Bayangan redup piringan bulan (earthshine)
                        drawCircle(
                            color = Color(0xFFCBD5E1).copy(alpha = 0.08f * moonPresence),
                            radius = moonRadius,
                            center = moonCenter
                        )

                        // Bentuk Bulan Sabit murni menggunakan selisih dua lingkaran
                        val outerMoon = Path().apply {
                            addOval(Rect(center = moonCenter, radius = moonRadius))
                        }
                        val innerCutout = Path().apply {
                            addOval(
                                Rect(
                                    center = Offset(
                                        x = moonCenter.x + moonRadius * 0.42f,
                                        y = moonCenter.y - moonRadius * 0.18f
                                    ),
                                    radius = moonRadius * 0.86f
                                )
                            )
                        }
                        val crescentMoon = Path.combine(PathOperation.Difference, outerMoon, innerCutout)
                        drawPath(
                            path = crescentMoon,
                            color = Color(0xFFFEF9C3).copy(alpha = moonPresence)
                        )
                    }

                    // 4) Objek MATAHARI (Terbit Pagi -> Siang Cerah -> Siang Terik) saat ada cahaya (> 50 lx)
                    val sunPresence = ((visualLux - 50f) / 45f).coerceIn(0f, 1f)
                    if (sunPresence > 0.01f) {
                        // Ketinggian matahari naik mengikuti Lux (rendah di ufuk saat pagi -> tinggi saat siang)
                        val elevationProgress = ((visualLux - 50f) / 1450f)
                            .coerceIn(0f, 1f)
                            .pow(0.48f)
                        val sunCenter = Offset(
                            x = size.width * (0.80f + 0.04f * elevationProgress),
                            y = size.height * (0.72f - 0.51f * elevationProgress)
                        )
                        val coreRadius = (14f + 6f * elevationProgress).dp.toPx()

                        // Warna matahari bertransisi dari jingga-keemasan pagi ke kuning terang siang
                        val sunCoreColor = lerp(Color(0xFFFB923C), Color(0xFFFEF08A), elevationProgress)
                        val sunRimColor = lerp(Color(0xFFF97316), Color(0xFFFACC15), elevationProgress)

                        // Berkas sinar matahari diagonal ke arah tanaman Hydrilla saat siang terang
                        val beamStrength = ((visualLux - 220f) / 1400f).coerceIn(0f, 1f) * sunPresence
                        if (beamStrength > 0.02f) {
                            val beamPath = Path().apply {
                                moveTo(sunCenter.x - coreRadius * 0.5f, sunCenter.y + coreRadius * 0.4f)
                                lineTo(sunCenter.x + coreRadius * 0.7f, sunCenter.y + coreRadius * 0.9f)
                                lineTo(size.width * 0.54f, horizonY)
                                lineTo(size.width * 0.28f, horizonY)
                                close()
                            }
                            drawPath(
                                path = beamPath,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFEF08A).copy(alpha = 0.22f * beamStrength),
                                        Color(0xFFFDE047).copy(alpha = 0.03f * beamStrength)
                                    ),
                                    start = sunCenter,
                                    end = Offset(size.width * 0.42f, horizonY)
                                )
                            )
                        }

                        // Halo pendar cahaya matahari
                        val haloRadius = coreRadius * (2.3f + 0.9f * elevationProgress)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    sunRimColor.copy(alpha = (0.42f + 0.30f * elevationProgress) * sunPresence),
                                    sunRimColor.copy(alpha = 0.14f * sunPresence),
                                    Color.Transparent
                                ),
                                center = sunCenter,
                                radius = haloRadius
                            ),
                            radius = haloRadius,
                            center = sunCenter
                        )

                        // Garis-garis pancaran sinar matahari berputar lembut
                        val rayCount = 12
                        val rayInner = coreRadius * 1.25f
                        val rayOuter = coreRadius * (1.55f + 0.45f * elevationProgress)
                        val baseAngle = animTimeSec * 0.35f
                        for (i in 0 until rayCount) {
                            val angle = baseAngle + (i * (Math.PI * 2.0 / rayCount)).toFloat()
                            val waveLen = if (i % 2 == 0) rayOuter else (rayInner + (rayOuter - rayInner) * 0.65f)
                            drawLine(
                                color = sunCoreColor.copy(alpha = (0.45f + 0.45f * elevationProgress) * sunPresence),
                                start = Offset(
                                    x = sunCenter.x + cos(angle) * rayInner,
                                    y = sunCenter.y + sin(angle) * rayInner
                                ),
                                end = Offset(
                                    x = sunCenter.x + cos(angle) * waveLen,
                                    y = sunCenter.y + sin(angle) * waveLen
                                ),
                                strokeWidth = 2.2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }

                        // Piringan inti Matahari
                        drawCircle(
                            color = sunRimColor.copy(alpha = sunPresence),
                            radius = coreRadius,
                            center = sunCenter
                        )
                        drawCircle(
                            color = sunCoreColor.copy(alpha = sunPresence),
                            radius = coreRadius * 0.72f,
                            center = sunCenter
                        )
                    }

                    // 5) Awan lembut yang ikut berubah nuansa warna (malam gelap -> pagi jingga -> siang putih)
                    val cloudColor = when {
                        visualLux < 55f -> Color(0xFF475569).copy(alpha = 0.18f)
                        visualLux < 260f -> lerp(
                            Color(0xFFFED7AA).copy(alpha = 0.42f),
                            Color.White.copy(alpha = 0.55f),
                            ((visualLux - 55f) / 205f).coerceIn(0f, 1f)
                        )
                        else -> Color.White.copy(alpha = 0.55f)
                    }
                    val cloudDrift = sin(animTimeSec * 0.25f) * 8.dp.toPx()
                    val c1x = size.width * 0.15f + cloudDrift
                    val c1y = size.height * 0.24f
                    drawCircle(cloudColor, radius = 11.dp.toPx(), center = Offset(c1x - 9.dp.toPx(), c1y + 2.dp.toPx()))
                    drawCircle(cloudColor, radius = 14.dp.toPx(), center = Offset(c1x + 2.dp.toPx(), c1y - 2.dp.toPx()))
                    drawCircle(cloudColor, radius = 10.dp.toPx(), center = Offset(c1x + 14.dp.toPx(), c1y + 3.dp.toPx()))

                    if (visualLux >= 55f) {
                        val c2x = size.width * 0.68f - cloudDrift * 0.7f
                        val c2y = size.height * 0.16f
                        drawCircle(cloudColor, radius = 8.dp.toPx(), center = Offset(c2x - 7.dp.toPx(), c2y + 2.dp.toPx()))
                        drawCircle(cloudColor, radius = 11.dp.toPx(), center = Offset(c2x + 2.dp.toPx(), c2y - 1.dp.toPx()))
                        drawCircle(cloudColor, radius = 8.dp.toPx(), center = Offset(c2x + 11.dp.toPx(), c2y + 2.dp.toPx()))
                    }

                    // 6) Siluet permukaan meja/ufuk bawah sebagai pijakan gelas kimia
                    val surfaceColor = lerp(Color(0xFF0B1325), Color(0xFF0F766E), (0.15f + 0.55f * brightnessRatio))
                    drawRect(
                        color = surfaceColor.copy(alpha = 0.45f),
                        topLeft = Offset(0f, horizonY),
                        size = Size(size.width, size.height - horizonY)
                    )

                    // 7) Gelas Kimia (Beaker) di sisi kiri-tengah agar Bulan/Matahari di kanan terlihat utuh
                    val cx = size.width * 0.43f
                    val beakerW = (size.width * 0.44f).coerceIn(124.dp.toPx(), 200.dp.toPx())
                    val beakerH = size.height * 0.76f
                    val beakerLeft = cx - beakerW / 2f
                    val beakerTop = size.height * 0.16f
                    val beakerBottom = beakerTop + beakerH

                    // Air di dalam gelas kimia
                    val waterTop = beakerTop + beakerH * 0.16f
                    val waterColor = lerp(Color(0xFF1E3A8A), Color(0xFF38BDF8), (0.30f + 0.70f * brightnessRatio).coerceIn(0f, 1f))
                    drawRoundRect(
                        color = waterColor.copy(alpha = 0.36f + 0.16f * brightnessRatio),
                        topLeft = Offset(beakerLeft + 4.dp.toPx(), waterTop),
                        size = Size(beakerW - 8.dp.toPx(), beakerBottom - waterTop - 3.dp.toPx()),
                        cornerRadius = CornerRadius(12.dp.toPx())
                    )

                    // Tanaman air Hydrilla di dasar corong
                    val plantBaseY = beakerBottom - 8.dp.toPx()
                    val stemColor = lerp(Color(0xFF14532D), Color(0xFF15803D), (0.35f + 0.65f * brightnessRatio).coerceIn(0f, 1f))
                    val leafColor = lerp(Color(0xFF166534), Color(0xFF22C55E), (0.35f + 0.65f * brightnessRatio).coerceIn(0f, 1f))
                    val stemOffsets = listOf(-22.dp.toPx(), -8.dp.toPx(), 8.dp.toPx(), 22.dp.toPx())
                    stemOffsets.forEachIndexed { idx, dx ->
                        val stemHeight = (34 + (idx % 2) * 10).dp.toPx()
                        val sx = cx + dx
                        drawLine(
                            color = stemColor,
                            start = Offset(sx, plantBaseY),
                            end = Offset(sx + dx * 0.15f, plantBaseY - stemHeight),
                            strokeWidth = 3.2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        // Daun-daun kecil Hydrilla
                        for (leafIdx in 1..4) {
                            val ly = plantBaseY - (stemHeight * leafIdx / 4.5f)
                            drawLine(
                                color = leafColor,
                                start = Offset(sx, ly),
                                end = Offset(sx - 8.dp.toPx(), ly - 4.dp.toPx()),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = leafColor,
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
                        val wobbleX = sin(b.progressY * 10f + b.wobblePhase) * 1.8.dp.toPx()
                        val bx = cx + b.xFraction * maxHalfWidth + wobbleX

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
