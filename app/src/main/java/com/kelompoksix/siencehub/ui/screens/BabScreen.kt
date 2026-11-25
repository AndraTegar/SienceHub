package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.BlokKonten
import com.kelompoksix.siencehub.data.models.SoalMini
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.data.repositories.IsiBabRepository
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SienceHubTheme
import com.kelompoksix.siencehub.ui.theme.StatusDanger
import com.kelompoksix.siencehub.ui.theme.StatusSuccess
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

@Composable
fun BabScreen(topikMateri: TopikMateri, babId: Int, onBackClick: () -> Unit) {
    val isi = remember(topikMateri.id, babId) { IsiBabRepository.getIsiBab(topikMateri.id, babId) }
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
                .padding(horizontal = metrics.horizontalPadding, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.White.copy(alpha = 0.18f), CircleShape)
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
                text = "${topikMateri.kategori} • Bab $babId",
                color = Color.White,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 52.dp)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SurfaceOffWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 720.dp) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(
                        horizontal = metrics.horizontalPadding,
                        vertical = 22.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isi == null) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Materi belum tersedia untuk bab ini.",
                                    color = TextSecondary,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    } else {
                        items(isi.blok) { BlokItem(it) }
                        if (isi.kuis.isNotEmpty()) {
                            item { KuisMiniCard(isi.kuis) }
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun BlokItem(blok: BlokKonten) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        when (blok) {
            is BlokKonten.TeksGambar -> Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                blok.gambar?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
                Text(
                    text = blok.teks,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            is BlokKonten.Rumus -> Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SageSoft.copy(alpha = 0.55f))
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = blok.judul,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = SagePrimary
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = blok.isi,
                        fontSize = 14.5.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                }
                blok.gambar?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.widthIn(max = 115.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            is BlokKonten.Gambar -> Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(blok.gambar),
                    contentDescription = blok.keterangan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.FillWidth
                )
                blok.keterangan?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        fontSize = 12.5.sp,
                        color = TextSecondary
                    )
                }
            }

            is BlokKonten.Paragraf -> Column(modifier = Modifier.padding(20.dp)) {
                blok.judul?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(6.dp))
                }
                Text(
                    text = blok.teks,
                    fontSize = 14.5.sp,
                    color = TextSecondary,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
private fun KuisMiniCard(soal: List<SoalMini>) {
    var index by remember { mutableIntStateOf(0) }
    var terpilih by remember { mutableStateOf<Int?>(null) }
    var skor by remember { mutableIntStateOf(0) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SagePrimary.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cek Pemahaman",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (index < soal.size) {
                    Surface(
                        shape = CircleShape,
                        color = SageSoft
                    ) {
                        Text(
                            text = "${index + 1}/${soal.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SagePrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (index >= soal.size) {
                Text(
                    text = "Skor kamu: $skor dari ${soal.size} benar!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Button(
                    onClick = { index = 0; skor = 0; terpilih = null },
                    colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ulangi Latihan")
                }
            } else {
                val s = soal[index]
                Text(
                    text = s.pertanyaan,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    lineHeight = 21.sp
                )

                s.opsi.forEachIndexed { i, teks ->
                    val bgColor = when {
                        terpilih == null -> SurfaceOffWhite
                        i == s.jawabanBenar -> Color(0xFFDCFCE7)
                        i == terpilih -> Color(0xFFFEE2E2)
                        else -> SurfaceOffWhite.copy(alpha = 0.6f)
                    }
                    val borderColor = when {
                        terpilih == null -> BorderSubtle
                        i == s.jawabanBenar -> StatusSuccess
                        i == terpilih -> StatusDanger
                        else -> BorderSubtle
                    }
                    val textColor = when {
                        terpilih == null -> TextPrimary
                        i == s.jawabanBenar -> Color(0xFF166534)
                        i == terpilih -> Color(0xFF991B1B)
                        else -> TextSecondary
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = bgColor,
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(enabled = terpilih == null) {
                                terpilih = i
                                if (i == s.jawabanBenar) skor++
                            }
                    ) {
                        Text(
                            text = teks,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }

                if (terpilih != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SageSoft,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = s.pembahasan,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 19.sp,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                    Button(
                        onClick = { index++; terpilih = null },
                        colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(if (index == soal.size - 1) "Lihat Skor" else "Soal Berikutnya")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BabScreenPreview() {
    SienceHubTheme {
        BabScreen(
            topikMateri = MateriRepository.getDaftarTopik()[1],
            babId = 1,
            onBackClick = {}
        )
    }
}
