package com.kelompoksix.siencehub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Beranda : Screen("beranda", "Beranda", Icons.Default.Home)
    object Materi : Screen("materi", "Daftar Materi", Icons.Default.Book)
    object Evaluasi : Screen("evaluasi", "Evaluasi", Icons.AutoMirrored.Filled.Assignment)
    object Profil : Screen("profil", "Profil", Icons.Default.Person)
}

val bottomNavItems = listOf(
    Screen.Beranda,
    Screen.Materi,
    Screen.Evaluasi,
    Screen.Profil
)
