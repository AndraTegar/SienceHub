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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight

// Struktur data untuk menyimpan informasi tiap menu
data class NavItem(
    val title: String,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
)

@Composable
fun FloatingBottomNav(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "Beranda",
        "Materi",
        "Profil"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEachIndexed { index, label ->

                NavigationItem(
                    label = label,
                    selected = selectedIndex == index,
                    onClick = {
                        onItemSelected(index)
                    }
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val warnaHijau = Color(0xFF869E83)

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = label,
            color = if (selected) warnaHijau else Color.Gray,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {

                FontWeight.Normal
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingBottomNavPreview() {
    // Ingat, state ini hanya untuk preview interaktif di Android Studio
    var indexAktif by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter // Memaksa navigasi ke bawah layar
    ) {
        FloatingBottomNav(
            selectedIndex = indexAktif,
            onItemSelected = { indexAktif = it },
        )
    }
}