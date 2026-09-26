package com.kelompoksix.siencehub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelompoksix.siencehub.ui.screens.LoginScreen
import com.kelompoksix.siencehub.ui.screens.SignupScreen
import com.kelompoksix.siencehub.ui.screens.SplashScreen
import com.kelompoksix.siencehub.ui.screens.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(navController = navController)
        }

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onContinueClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // TODO: Arahkan ke Beranda setelah login sukses
                },
                onNavigateToSignup = {
                    navController.navigate(Routes.SIGNUP)
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                onCreateAccountSuccess = {
                    // TODO: Aksi saat akun berhasil dibuat
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}