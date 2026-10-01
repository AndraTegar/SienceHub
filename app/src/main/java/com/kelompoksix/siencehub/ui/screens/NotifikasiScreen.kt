package com.kelompoksix.siencehub.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.Notifikasi
import com.kelompoksix.siencehub.data.models.TipeNotifikasi
import com.kelompoksix.siencehub.data.repositories.NotifikasiRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    onBackClick: () -> Unit
) {
    val sageGreen = Color(0xFF7A8B76)
    val notifikasiList by NotifikasiRepository.notifikasiList.collectAsState()

    var filterSelected by remember { mutableIntStateOf(0) } // 0: Semua, 1: Belum Dibaca

    val listFiltered = remember(notifikasiList, filterSelected) {
        when (filterSelected) {
            1 -> notifikasiList.filter { !it.isDibaca }
            else -> notifikasiList
        }
    }

    val unreadCount = remember(notifikasiList) {
        notifikasiList.count { !it.isDibaca }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(sageGreen)
    ) {
        // Header Notifikasi
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(50))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Kembali",
                        tint = sageGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Notifikasi",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (unreadCount > 0) {
                        Text(
                            text = "$unreadCount belum dibaca",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Tombol Tandai Semua Dibaca
            if (unreadCount > 0) {
                TextButton(
                    onClick = { NotifikasiRepository.tandaiSemuaDibaca() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Baca Semua",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Body Notifikasi (Surface Putih)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFAFAFA),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                // Filter Chip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterChip(
                        selected = filterSelected == 0,
                        onClick = { filterSelected = 0 },
                        label = { Text("Semua (${notifikasiList.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = sageGreen,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color.Gray
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = filterSelected == 0,
                            borderColor = Color.LightGray.copy(alpha = 0.5f),
                            selectedBorderColor = sageGreen
                        )
                    )

                    FilterChip(
                        selected = filterSelected == 1,
                        onClick = { filterSelected = 1 },
                        label = { Text("Belum Dibaca ($unreadCount)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = sageGreen,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color.Gray
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = filterSelected == 1,
                            borderColor = Color.LightGray.copy(alpha = 0.5f),
                            selectedBorderColor = sageGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (listFiltered.isEmpty()) {
                    // Tampilan Jika Kosong
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(sageGreen.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsNone,
                                    contentDescription = null,
                                    tint = sageGreen,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (filterSelected == 1) "Tidak ada notifikasi belum dibaca" else "Belum ada notifikasi",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Pemberitahuan aktivitas, materi baru, dan pencapaian kamu akan muncul di sini.",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }
                } else {
                    // Daftar Notifikasi
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(
                            items = listFiltered,
                            key = { it.id }
                        ) { item ->
                            ItemNotifikasiCard(
                                notifikasi = item,
                                onClick = {
                                    NotifikasiRepository.tandaiDibaca(item.id)
                                },
                                onDelete = {
                                    NotifikasiRepository.hapusNotifikasi(item.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemNotifikasiCard(
    notifikasi: Notifikasi,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (notifikasi.isDibaca) Color.White else Color(0xFFF3F6F2),
        label = "bgColor"
    )

    val (icon, iconBgColor, iconTint) = when (notifikasi.tipe) {
        TipeNotifikasi.STREAK -> Triple(Icons.Default.LocalFireDepartment, Color(0xFFFFF3E0), Color(0xFFFF6D00))
        TipeNotifikasi.MATERI -> Triple(Icons.Default.Book, Color(0xFFE1F5FE), Color(0xFF0288D1))
        TipeNotifikasi.KUIS -> Triple(Icons.Default.Quiz, Color(0xFFF3E5F5), Color(0xFF8E24AA))
        TipeNotifikasi.EKSPERIMEN -> Triple(Icons.Default.Science, Color(0xFFE8ECE7), Color(0xFF7A8B76))
        TipeNotifikasi.INFO -> Triple(Icons.Default.Lightbulb, Color(0xFFFFFDE7), Color(0xFFFBC02D))
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notifikasi.isDibaca) 1.dp else 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Ikon Jenis Notifikasi
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Isi Notifikasi
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notifikasi.judul,
                        fontWeight = if (notifikasi.isDibaca) FontWeight.SemiBold else FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Dot belum dibaca
                    if (!notifikasi.isDibaca) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF7A8B76))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notifikasi.pesan,
                    fontSize = 13.sp,
                    color = Color.DarkGray.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notifikasi.waktu,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Tombol Hapus Single Notifikasi
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Hapus",
                    tint = Color.Gray.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
