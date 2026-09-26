package com.kelompoksix.siencehub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelompoksix.siencehub.ui.screens.LoginScreen
import com.kelompoksix.siencehub.ui.screens.WelcomeScreen

@Composable
fun AppNavigation() {
    // Membuat kontroler navigasi
    val navController = rememberNavController()

    // NavHost adalah wadah untuk menukar layar. Layar pertama (start) adalah WELCOME.
    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        // Daftarkan Welcome Screen
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onContinueClick = {
                    // Perintah berpindah ke halaman Login
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        // Daftarkan Login Screen
        composable(Routes.LOGIN) {
            LoginScreen()
        }
    }
}