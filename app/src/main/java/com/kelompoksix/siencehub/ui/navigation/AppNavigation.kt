package com.kelompoksix.siencehub.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kelompoksix.siencehub.ui.screens.BerandaScreen
import com.kelompoksix.siencehub.ui.screens.MateriScreen
import com.kelompoksix.siencehub.ui.screens.ProfilScreen
import com.kelompoksix.siencehub.ui.theme.GreenBackground

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Beranda.route

    // Scaffold tidak lagi menggunakan bottomBar
    Scaffold { innerPadding ->

        // Gunakan Box agar konten bisa ditumpuk (Z-Index)
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // ==========================================
            // LAYER BELAKANG: Konten Navigasi (NavHost)
            // ==========================================
            NavHost(
                navController = navController,
                startDestination = Screen.Beranda.route,
                // Gunakan padding atas saja agar status bar tidak tertutup,
                // tapi biarkan konten tembus ke paling bawah layar.
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                composable(Screen.Beranda.route) {
                    BerandaScreen(
                        onNavigateToMateri = {
                            navController.navigate(Screen.Materi.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable(Screen.Materi.route) {
                    MateriScreen()
                }
                composable(Screen.Profil.route) {
                    ProfilScreen()
                }
            }

            // ==========================================
            // LAYER DEPAN: Navbar Melayang (Floating)
            // ==========================================
            FigmaBottomBar(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                // Paksa posisi navbar ke bawah tengah
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun FigmaBottomBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier // Tambahkan modifier agar posisinya bisa diatur dari luar
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Menghindari tombol/gesture bawaan Android
            .padding(bottom = 10.dp, start = 15.dp, end = 15.dp), // Jarak melayang dari tepi layar
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 16.dp,
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E5E5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { screen ->
                val isSelected = currentRoute == screen.route

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigate(screen) }
                ) {
                    if (isSelected) {
                        // Selected Item (Green pill background icon + label + green indicator bar)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GreenBackground,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = screen.title,
                            color = GreenBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        // Curved indicator line under active tab
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(50))
                                .background(GreenBackground)
                        )
                    } else {
                        // Unselected Item (Muted outline icon)
                        Box(
                            modifier = Modifier.size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = Color(0xFF8A95A5),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)

@Composable

fun AppNavigationPreview() {

    com.kelompoksix.siencehub.ui.theme.SienceHubTheme {

        AppNavigation()

    }

}