package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Struktur data untuk menyimpan informasi tiap menu
data class NavItem(
    val title: String,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
)

@Composable
fun FloatingBottomNav(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavItem("Materi", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        NavItem("Profil", Icons.Filled.Person, Icons.Outlined.Person)
    )

    // Surface bertindak sebagai pembungkus kapsul putih
    Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 8.dp, // Efek bayangan agar terlihat melayang
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp) // Jarak dari tepi layar (kiri-kanan & bawah)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val warnaAktif = Color(0xFF869E83) // Hijau sage
                val warnaNonAktif = Color.LightGray

                Box(
                    modifier = Modifier
                        .clip(CircleShape) // Efek ripple (gelombang saat diklik) berbentuk bulat
                        .clickable { onItemSelected(index) }
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        // Mengubah ikon menjadi tebal (filled) jika sedang aktif
                        imageVector = if (isSelected) item.iconSelected else item.iconUnselected,
                        contentDescription = item.title,
                        tint = if (isSelected) warnaAktif else warnaNonAktif,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingBottomNavPreview() {
    // Ingat, state ini hanya untuk preview interaktif di Android Studio
    var indexAktif by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter // Memaksa navigasi ke bawah layar
    ) {
        FloatingBottomNav(
            selectedIndex = indexAktif,
            onItemSelected = { indexAktif = it }
        )
    }
}