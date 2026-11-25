package com.kelompoksix.siencehub.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Book
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.Notifikasi
import com.kelompoksix.siencehub.data.models.TipeNotifikasi
import com.kelompoksix.siencehub.data.repositories.NotifikasiRepository
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    onBackClick: () -> Unit
) {
    val notifikasiList by NotifikasiRepository.notifikasiList.collectAsState()
    val metrics = rememberResponsiveMetrics()

    var filterSelected by remember { mutableIntStateOf(0) }

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
            .background(SagePrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    onClick = onBackClick,
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Kembali",
                            tint = SagePrimary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Notifikasi",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (unreadCount > 0) {
                        Text(
                            text = "$unreadCount belum dibaca",
                            color = Color.White.copy(alpha = 0.82f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (unreadCount > 0) {
                Surface(
                    onClick = { NotifikasiRepository.tandaiSemuaDibaca() },
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.18f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Baca Semua",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
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
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 680.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = metrics.horizontalPadding, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilterChip(
                            selected = filterSelected == 0,
                            onClick = { filterSelected = 0 },
                            label = { Text("Semua (${notifikasiList.size})") },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SagePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceWhite,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = filterSelected == 0,
                                borderColor = BorderSubtle,
                                selectedBorderColor = SagePrimary
                            )
                        )

                        FilterChip(
                            selected = filterSelected == 1,
                            onClick = { filterSelected = 1 },
                            label = { Text("Belum Dibaca ($unreadCount)") },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SagePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceWhite,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = filterSelected == 1,
                                borderColor = BorderSubtle,
                                selectedBorderColor = SagePrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (listFiltered.isEmpty()) {
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
                                        .size(76.dp)
                                        .background(SageSoft, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsNone,
                                        contentDescription = null,
                                        tint = SagePrimary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (filterSelected == 1) "Tidak ada notifikasi belum dibaca" else "Belum ada notifikasi",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.5.sp,
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Pemberitahuan aktivitas, materi baru, dan pencapaian kamu akan muncul di sini.",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )
                            }
                        }
                    } else {
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
                                    onClick = { NotifikasiRepository.tandaiDibaca(item.id) },
                                    onDelete = { NotifikasiRepository.hapusNotifikasi(item.id) }
                                )
                            }
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
        targetValue = if (notifikasi.isDibaca) SurfaceWhite else SageSoft,
        label = "bgColor"
    )

    val (icon, iconBgColor, iconTint) = when (notifikasi.tipe) {
        TipeNotifikasi.STREAK -> Triple(Icons.Default.LocalFireDepartment, Color(0xFFFFF3E0), Color(0xFFFF6D00))
        TipeNotifikasi.MATERI -> Triple(Icons.Default.Book, Color(0xFFE1F5FE), Color(0xFF0288D1))
        TipeNotifikasi.KUIS -> Triple(Icons.Default.Quiz, Color(0xFFF3E5F5), Color(0xFF8E24AA))
        TipeNotifikasi.EKSPERIMEN -> Triple(Icons.Default.Science, SageSoft, SagePrimary)
        TipeNotifikasi.INFO -> Triple(Icons.Default.Lightbulb, Color(0xFFFFFDE7), Color(0xFFFBC02D))
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notifikasi.isDibaca) 0.dp else 2.dp),
        border = BorderStroke(
            1.dp,
            if (notifikasi.isDibaca) BorderSubtle else SagePrimary.copy(alpha = 0.28f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
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
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notifikasi.judul,
                        fontWeight = if (notifikasi.isDibaca) FontWeight.SemiBold else FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (!notifikasi.isDibaca) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SagePrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notifikasi.pesan,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notifikasi.waktu,
                    fontSize = 11.sp,
                    color = TextSecondary.copy(alpha = 0.75f)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Hapus",
                    tint = TextSecondary.copy(alpha = 0.65f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
