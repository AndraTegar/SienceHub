package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.BlokKonten
import com.kelompoksix.siencehub.data.models.SoalMini
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.data.repositories.IsiBabRepository
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.ui.theme.SienceHubTheme

private val BgHijau = Color(0xFF7D9078)
private val CardAbu = Color(0xFFDADADA)
private val Salmon = Color(0xFFFF7F7F)
private val Benar = Color(0xFF6FCF97)
private val Salah = Color(0xFFD64545)

@Composable
fun BabScreen(topikMateri: TopikMateri, babId: Int, onBackClick: () -> Unit) {
    val isi = remember(topikMateri.id, babId) { IsiBabRepository.getIsiBab(topikMateri.id, babId) }

    Column(Modifier.fillMaxSize().background(BgHijau)) {
        Spacer(Modifier.height(44.dp))
        Box(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier.fillMaxWidth().height(48.dp)
                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, "Kembali", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Text("${topikMateri.kategori} > Bab $babId", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isi == null) {
                item { Text("Materi belum tersedia", color = Color.White) }
            } else {
                items(isi.blok) { BlokItem(it) }
                if (isi.kuis.isNotEmpty()) item { KuisMiniCard(isi.kuis) }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun BlokItem(blok: BlokKonten) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardAbu),
        modifier = Modifier.fillMaxWidth()
    ) {
        when (blok) {
            is BlokKonten.TeksGambar -> Row(
                Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                blok.gambar?.let { Image(painterResource(it), null, Modifier.size(100.dp), contentScale = ContentScale.Fit) }
                Text(blok.teks, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            }

            is BlokKonten.Rumus -> Row(
                Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(blok.judul, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(Modifier.height(6.dp))
                    Text(blok.isi, fontSize = 15.sp, color = Color.DarkGray)
                }
                blok.gambar?.let { Image(painterResource(it), null, Modifier.width(130.dp), contentScale = ContentScale.Fit) }
            }

            is BlokKonten.Gambar -> Column(Modifier.padding(16.dp)) {
                Image(painterResource(blok.gambar), blok.keterangan, Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
                blok.keterangan?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, fontSize = 12.sp, color = Color.DarkGray)
                }
            }

            is BlokKonten.Paragraf -> Column(Modifier.padding(20.dp)) {
                blok.judul?.let {
                    Text(it, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(Modifier.height(6.dp))
                }
                Text(blok.teks, fontSize = 15.sp, color = Color.DarkGray, lineHeight = 22.sp)
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardAbu),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Cek Pemahaman", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            if (index >= soal.size) {
                Text("Skor kamu: $skor / ${soal.size}", fontSize = 15.sp, color = Color.DarkGray)
                Button(onClick = { index = 0; skor = 0; terpilih = null }) { Text("Ulangi") }
            } else {
                val s = soal[index]
                Text("${index + 1}/${soal.size}. ${s.pertanyaan}", fontSize = 15.sp, color = Color.Black)

                s.opsi.forEachIndexed { i, teks ->
                    val warna = when {
                        terpilih == null -> Salmon
                        i == s.jawabanBenar -> Benar
                        i == terpilih -> Salah
                        else -> Salmon.copy(alpha = 0.5f)
                    }
                    Box(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(50.dp)).background(warna)
                            .clickable(enabled = terpilih == null) {
                                terpilih = i
                                if (i == s.jawabanBenar) skor++
                            }
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                    ) { Text(teks, fontSize = 14.sp, color = Color.Black) }
                }

                if (terpilih != null) {
                    Text(s.pembahasan, fontSize = 13.sp, color = Color.DarkGray)
                    Button(onClick = { index++; terpilih = null }) {
                        Text(if (index == soal.size - 1) "Lihat skor" else "Lanjut")
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