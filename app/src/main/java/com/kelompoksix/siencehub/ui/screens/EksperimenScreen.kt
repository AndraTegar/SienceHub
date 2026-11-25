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
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.theme.AccentBlue
import com.kelompoksix.siencehub.ui.theme.AccentBlueSoft
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

private data class MenuEksperimen(
    val id: String,
    val mapel: String,
    val judul: String,
    val ikon: ImageVector,
    val tersedia: Boolean
)

private val daftarEksperimen = listOf(
    MenuEksperimen("fisika_newton2", "Fisika", "Simulasi Hukum II Newton", Icons.Default.Speed, true),
    MenuEksperimen("astronomi_gravitasi", "Astronomi", "Gravitasi di berbagai planet", Icons.Default.Public, true),
    MenuEksperimen("kimia_ph", "Kimia", "Uji pH dengan indikator", Icons.Default.Science, true),
    MenuEksperimen("matematika_timbangan", "Matematika", "Timbangan persamaan", Icons.Default.Balance, true)
)

@Composable
fun EksperimenScreen(onBackClick: () -> Unit, onPilih: (String) -> Unit) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.White.copy(alpha = 0.22f), CircleShape)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Kembali",
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
            Text(
                text = "EKSPERIMEN VIRTUAL",
                color = Color.White,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                letterSpacing = 0.6.sp
            )
        }

        Spacer(Modifier.height(10.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SurfaceOffWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 680.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(horizontal = metrics.horizontalPadding, vertical = 26.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Pilih Laboratorium Simulasi",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Uji konsep sains secara interaktif melalui simulasi visual real-time.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    daftarEksperimen.forEach { m ->
                        Card(
                            onClick = {
                                if (m.tersedia) onPilih(m.id)
                                else Toast.makeText(context, "Segera hadir", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp, AccentBlueSoft),
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(if (m.tersedia) 1f else 0.6f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = AccentBlueSoft,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = m.ikon,
                                            contentDescription = null,
                                            tint = AccentBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = m.mapel.uppercase(),
                                        color = AccentBlue,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = m.judul,
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
