package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.data.repositories.PencarianRepository

@Composable
fun SearchBarMateri(
    modifier: Modifier = Modifier,
    onHasilClick: (TopikMateri, Int?) -> Unit = { _, _ -> }
) {
    var query by remember { mutableStateOf("") }
    val hasil = remember(query) { PencarianRepository.cari(query) }
    val focus = LocalFocusManager.current

    var barWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = modifier.onSizeChanged {
            barWidth = with(density) { it.width.toDp() }
        }
    ) {
        // Menggunakan BasicTextField agar teks tidak terpotong saat tingginya kurang dari 56dp
        BasicTextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
            cursorBrush = SolidColor(Color.White), // Mengubah kursor ketik menjadi putih
            modifier = Modifier.fillMaxSize(),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp) // Padding horizontal mandiri
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // Box penampung Teks Placeholder dan InnerTextField agar saling bertumpuk rapi
                    Box(modifier = Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                text = "Cari mapel atau bab...",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }

                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { query = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        )

        if (query.isNotBlank()) {
            Popup(
                alignment = Alignment.TopStart,
                // Offset disesuaikan menjadi 54dp (46dp tinggi bar + 8dp margin) agar jarak pop-up presisi
                offset = IntOffset(0, with(density) { 54.dp.roundToPx() }),
                properties = PopupProperties(focusable = false)
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.width(barWidth)
                ) {
                    if (hasil.isEmpty()) {
                        Text(
                            text = "Tidak ditemukan",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        Column {
                            hasil.take(6).forEach { h ->
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            focus.clearFocus()
                                            query = ""
                                            onHasilClick(h.topik, h.bab?.id)
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                ) {
                                    Text(
                                        text = h.bab?.judulBab ?: h.topik.judul,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (h.bab != null) "${h.topik.kategori} • Bab ${h.bab.id}" else "Mapel • ${h.topik.kategori}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}