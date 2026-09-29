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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

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
    val sageGreen = MaterialTheme.colorScheme.primary
    val lightSage = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)

    val items = listOf(
        NavItem("Beranda", Icons.Default.Home),
        NavItem("Materi", Icons.Default.MenuBook),
        NavItem("Profil", Icons.Default.Person)
    )

    Row(
        modifier = modifier
            .width(250.dp)
            .height(64.dp)
            .shadow(elevation = 12.dp, shape = CircleShape)
            .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index

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
                    tint = if (isSelected) sageGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
