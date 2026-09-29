package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(modifier) {
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

        if (query.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (hasil.isEmpty()) {
                    Text("Tidak ditemukan", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(16.dp))
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
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    h.bab?.judulBab ?: h.topik.judul,
                                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black
                                )
                                Text(
                                    if (h.bab != null) "${h.topik.kategori} • Bab ${h.bab.id}" else "Mapel • ${h.topik.kategori}",
                                    fontSize = 12.sp, color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}