package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
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

    // 1. Menyimpan lebar kotak pencarian untuk disamakan dengan lebar Popup
    var barWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // 2. Ganti Column dengan Box agar elemen bisa bertumpuk
    Box(
        modifier = modifier.onSizeChanged {
            // Mengambil ukuran lebar TextField secara dinamis saat di-render
            barWidth = with(density) { it.width.toDp() }
        }
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            placeholder = { Text("Cari mapel atau bab...", color = Color.White.copy(alpha = 0.8f)) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.White) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, "Hapus", tint = Color.White)
                    }
                }
            },
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.25f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.25f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Jika ada teks, tampilkan Popup yang melayang
        if (query.isNotBlank()) {
            Popup(
                alignment = Alignment.TopStart,
                // Menggeser Popup ke bawah sejauh tinggi TextField (kira-kira 56dp) + margin 8dp
                offset = IntOffset(0, with(density) { 64.dp.roundToPx() }),
                // focusable = false agar keyboard tidak tertutup otomatis saat Popup muncul
                properties = PopupProperties(focusable = false)
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    // Memberikan bayangan agar terlihat benar-benar melayang (Pop-out effect)
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    // Menyamakan lebar hasil dengan kotak pencarian di atasnya
                    modifier = Modifier.width(barWidth)
                ) {
                    if (hasil.isEmpty()) {
                        Text(
                            "Tidak ditemukan",
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
                                        h.bab?.judulBab ?: h.topik.judul,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        if (h.bab != null) "${h.topik.kategori} • Bab ${h.bab.id}" else "Mapel • ${h.topik.kategori}",
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