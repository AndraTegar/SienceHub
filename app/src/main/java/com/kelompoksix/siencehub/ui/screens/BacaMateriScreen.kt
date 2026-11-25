package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.BlokKonten
import com.kelompoksix.siencehub.data.models.IsiBab
import com.kelompoksix.siencehub.ui.theme.AccentBlue
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

@Composable
fun BacaMateriScreen(
    judulBab: String,
    isiBab: IsiBab,
    onBackClick: () -> Unit,
    onMulaiKuisClick: () -> Unit = {}
) {
    val darkChalkBg = Color(0xFF1A202C)
    val metrics = rememberResponsiveMetrics()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkChalkBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Kembali",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = judulBab,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 720.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(horizontal = metrics.horizontalPadding, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    isiBab.blok.forEach { blok ->
                        when (blok) {
                            is BlokKonten.Paragraf -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (!blok.judul.isNullOrEmpty()) {
                                        Text(
                                            text = blok.judul,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentBlue
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                    Text(
                                        text = blok.teks,
                                        fontSize = 14.5.sp,
                                        color = TextSecondary,
                                        lineHeight = 22.sp
                                    )
                                }
                            }

                            is BlokKonten.TeksGambar -> {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (blok.gambar != null) {
                                            Image(
                                                painter = painterResource(id = blok.gambar),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(72.dp)
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                        Text(
                                            text = blok.teks,
                                            fontSize = 14.sp,
                                            color = TextPrimary,
                                            lineHeight = 20.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }

                            is BlokKonten.Rumus -> {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        if (blok.judul.isNotEmpty()) {
                                            Text(
                                                text = blok.judul,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = Color(0xFF6B21A8)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                        }
                                        Text(
                                            text = blok.isi,
                                            fontSize = 14.sp,
                                            color = Color(0xFF4C1D95),
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }

                            is BlokKonten.Gambar -> {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = blok.gambar),
                                        contentDescription = blok.keterangan,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 160.dp, max = 240.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    if (!blok.keterangan.isNullOrEmpty()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = blok.keterangan,
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (isiBab.kuis.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onMulaiKuisClick,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text = "Lanjut ke Kuis",
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
