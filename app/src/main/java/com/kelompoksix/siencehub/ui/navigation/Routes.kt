package com.kelompoksix.siencehub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Beranda : Screen("beranda", "Home", Icons.Default.Home)
    object Materi : Screen("materi", "Materi", Icons.Outlined.Book)
    object Profil : Screen("profil", "Profil", Icons.Outlined.Person)
}

val bottomNavItems = listOf(
    Screen.Beranda,
    Screen.Materi,
    Screen.Profil
)
