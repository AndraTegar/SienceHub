package com.kelompoksix.siencehub.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.PilihanChip
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

private data class MenuEksperimen(
    val id: String,
    val mapel: String,
    val judul: String,
    val deskripsi: String,
    val ikon: ImageVector,
    val warnaAksen: Color,
    val labelSensor: String? = null,
    val tersedia: Boolean = true
)

private val daftarEksperimen = listOf(
    MenuEksperimen(
        id = "biologi_fotosintesis",
        mapel = "Biologi",
        judul = "Laju Fotosintesis (Ingenhousz)",
        deskripsi = "Ukur intensitas cahaya (Lux) secara langsung untuk mengamati produksi gelembung O₂ tanaman air.",
        ikon = Icons.Default.Eco,
        warnaAksen = Color(0xFF16A34A),
        labelSensor = "Sensor Cahaya"
    ),
    MenuEksperimen(
        id = "fisika_newton2",
        mapel = "Fisika",
        judul = "Simulasi Hukum II Newton",
        deskripsi = "Miringkan HP atau atur gaya dorong untuk menguji hubungan F = m × a secara real-time.",
        ikon = Icons.Default.Speed,
        warnaAksen = Color(0xFF2563EB),
        labelSensor = "Accelerometer"
    ),
    MenuEksperimen(
        id = "astronomi_gravitasi",
        mapel = "Astronomi",
        judul = "Gravitasi di Berbagai Planet",
        deskripsi = "Bandingkan gerak jatuh bebas dan berat benda di Bulan, Bumi, Mars, hingga Jupiter.",
        ikon = Icons.Default.Public,
        warnaAksen = Color(0xFF7C3AED)
    ),
    MenuEksperimen(
        id = "kimia_ph",
        mapel = "Kimia",
        judul = "Uji pH dengan Indikator",
        deskripsi = "Teteskan kubis ungu, lakmus, dan fenolftalein untuk mengidentifikasi sifat asam-basa larutan.",
        ikon = Icons.Default.Science,
        warnaAksen = Color(0xFF0891B2)
    ),
    MenuEksperimen(
        id = "matematika_timbangan",
        mapel = "Matematika",
        judul = "Timbangan Persamaan Linear",
        deskripsi = "Temukan nilai variabel x yang menyeimbangkan lengan neraca persamaan aljabar.",
        ikon = Icons.Default.Balance,
        warnaAksen = Color(0xFFD97706)
    )
)

@Composable
fun EksperimenScreen(onBackClick: () -> Unit, onPilih: (String) -> Unit) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()
    var selectedFilter by remember { mutableIntStateOf(0) } // 0 = Semua, 1 = Sensor HP, 2 = Simulasi

    val filteredList = remember(selectedFilter) {
        when (selectedFilter) {
            1 -> daftarEksperimen.filter { it.labelSensor != null }
            2 -> daftarEksperimen.filter { it.labelSensor == null }
            else -> daftarEksperimen
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        ResponsiveContentContainer(maxWidth = 840.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = metrics.horizontalPadding, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onBackClick,
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.20f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Kembali",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Eksperimen Virtual",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "${daftarEksperimen.size} Laboratorium Interaktif • 2 Sensor Fisik HP",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SurfaceOffWhite,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 840.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(horizontal = metrics.horizontalPadding, vertical = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Filter Kategori Eksperimen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PilihanChip(
                            teks = "Semua (${daftarEksperimen.size})",
                            terpilih = selectedFilter == 0,
                            onClick = { selectedFilter = 0 }
                        )
                        PilihanChip(
                            teks = "Sensor HP (2)",
                            terpilih = selectedFilter == 1,
                            onClick = { selectedFilter = 1 }
                        )
                        PilihanChip(
                            teks = "Visual (3)",
                            terpilih = selectedFilter == 2,
                            onClick = { selectedFilter = 2 }
                        )
                    }

                    // Layout Responsif: 2 Kolom di Tablet / Landscape, 1 Kolom di Ponsel
                    if (metrics.isTablet) {
                        filteredList.chunked(2).forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                pair.forEach { item ->
                                    EksperimenMenuCard(
                                        item = item,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            if (item.tersedia) onPilih(item.id)
                                            else Toast.makeText(context, "Segera hadir", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                                if (pair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } else {
                        filteredList.forEach { item ->
                            EksperimenMenuCard(
                                item = item,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    if (item.tersedia) onPilih(item.id)
                                    else Toast.makeText(context, "Segera hadir", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun EksperimenMenuCard(
    item: MenuEksperimen,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.dp,
            if (item.labelSensor != null) item.warnaAksen.copy(alpha = 0.35f) else BorderSubtle
        ),
        modifier = modifier.alpha(if (item.tersedia) 1f else 0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = item.warnaAksen.copy(alpha = 0.14f),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = item.ikon,
                        contentDescription = null,
                        tint = item.warnaAksen,
                        modifier = Modifier.size(27.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = item.mapel.uppercase(),
                        color = item.warnaAksen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    )
                    if (item.labelSensor != null) {
                        Surface(
                            shape = CircleShape,
                            color = item.warnaAksen.copy(alpha = 0.13f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sensors,
                                    contentDescription = null,
                                    tint = item.warnaAksen,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = item.labelSensor,
                                    color = item.warnaAksen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.judul,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.deskripsi,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}
