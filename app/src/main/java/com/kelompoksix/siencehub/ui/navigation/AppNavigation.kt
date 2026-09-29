package com.kelompoksix.siencehub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelompoksix.siencehub.ui.screens.LoginScreen
import com.kelompoksix.siencehub.ui.screens.SignupScreen
import com.kelompoksix.siencehub.ui.screens.SplashScreen
import com.kelompoksix.siencehub.ui.screens.WelcomeScreen
import com.kelompoksix.siencehub.ui.screens.KerangkaAplikasi

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
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    // Mengarahkan ke Kerangka Aplikasi (Home) setelah login sukses
                    navController.navigate(Routes.HOME) {
                        // Menghapus riwayat login agar saat di-back dari beranda tidak kembali ke form login
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Routes.SIGNUP)
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                onCreateAccountSuccess = {
                    // Langsung diarahkan ke Login setelah akun jadi
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ==========================================
        // RUTE UTAMA: KERANGKA APLIKASI & LOGOUT
        // ==========================================
        composable(Routes.HOME) {
            KerangkaAplikasi(
                onLogout = {
                    // Kembali ke halaman Login saat tombol Log Out ditekan
                    navController.navigate(Routes.LOGIN) {
                        // popUpTo(0) akan membersihkan seluruh riwayat halaman.
                        // Ini memastikan pengguna tidak bisa menekan tombol 'Back' di HP
                        // untuk kembali masuk secara diam-diam tanpa login ulang.
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

    }
}