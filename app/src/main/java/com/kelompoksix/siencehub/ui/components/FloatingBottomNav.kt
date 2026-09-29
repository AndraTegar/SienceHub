package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Data Class untuk menampung ikon vektor
data class NavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun FloatingBottomNav(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sageGreen = Color(0xFF7A8B76)
    val lightSage = Color(0xFFE8ECE7) // Warna latar belakang lembut saat ikon aktif

    val items = listOf(
        NavItem("Beranda", Icons.Default.Home),
        NavItem("Materi", Icons.Default.MenuBook),
        NavItem("Profil", Icons.Default.Person)
    )

    // Desain Wadah Navbar Melayang yang lebih kompak tanpa teks
    Row(
        modifier = modifier
            .width(220.dp) // Lebar disesuaikan agar pas dan elegan tanpa teks
            .height(64.dp)
            .shadow(elevation = 12.dp, shape = CircleShape) // Efek melayang
            .background(Color.White, shape = CircleShape)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index

            // Kotak pembungkus ikon dengan efek latar belakang saat dipilih
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) lightSage else Color.Transparent)
                    .clickable { onItemSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (isSelected) sageGreen else Color.Gray,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}