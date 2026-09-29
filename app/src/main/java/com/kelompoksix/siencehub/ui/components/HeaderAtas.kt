package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderAtas(
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    // Row membentang penuh ke kanan-kiri
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically, // Rata tengah secara vertikal
        horizontalArrangement = Arrangement.SpaceBetween // Memberi jarak maksimal antar elemen
    ) {

        // 1. Avatar Profil (Bentuk Lingkaran)
        Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onProfileClick() }
        ) {
            // Karena belum ada gambar rubah, kita pakai ikon orang bawaan dulu
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profil",
                tint = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 2. Search Bar Kapsul (Mendominasi ruang tengah)
        Surface(
            shape = CircleShape, // Membuat ujungnya melengkung penuh seperti kapsul
            color = Color(0xFFE5DCCC), // Warna beige/krem dari desain Figma
            modifier = Modifier
                .weight(1f) // Membuat Search Bar mengisi seluruh sisa ruang kosong di tengah
                .height(44.dp)
                .clickable { onSearchClick() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ikon Cari",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 3. Ikon Lonceng Notifikasi
        IconButton(
            onClick = { onNotificationClick() },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifikasi",
                tint = Color.White, // Menyesuaikan dengan latar belakang hijau
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF869E83) // Background hijau sage untuk preview
@Composable
fun HeaderAtasPreview() {
    HeaderAtas(
        onSearchClick = {},
        onNotificationClick = {},
        onProfileClick = {}
    )
}