package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun SearchBarMateri(
    query: String,
    onQueryChange: (String) -> Unit,
    hasilPencarian: List<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    var barSize by remember { mutableStateOf(IntSize.Zero) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier.onSizeChanged { barSize = it }
    ) {
        // Kotak Ketik Utama (TextField)
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Cari materi, kuis...", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Cari", tint = Color.Gray)
            },
            trailingIcon = {
                if (query.isNotEmpty() || isFocused) {
                    IconButton(onClick = {
                        onQueryChange("")
                        focusManager.clearFocus()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Color.Gray)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
        )

        // Overlay Hasil Pencarian (Popup)
        if (isFocused && query.isNotEmpty()) {
            Popup(
                alignment = androidx.compose.ui.Alignment.TopStart,
                properties = PopupProperties(focusable = false)
            ) {
                val widthInDp = with(LocalDensity.current) { barSize.width.toDp() }

                Box(
                    modifier = Modifier
                        .width(widthInDp)
                        .padding(top = 60.dp) // Jarak agar tidak bertumpuk dengan search bar
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 250.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            if (hasilPencarian.isEmpty()) {
                                item {
                                    Text(
                                        text = "Tidak ada hasil ditemukan.",
                                        color = Color.Gray,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                items(hasilPencarian) { hasil ->
                                    Text(
                                        text = hasil,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onItemClick(hasil)
                                                onQueryChange("")
                                                focusManager.clearFocus()
                                            }
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
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