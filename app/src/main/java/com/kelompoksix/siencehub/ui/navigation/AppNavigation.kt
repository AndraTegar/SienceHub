package com.kelompoksix.siencehub.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kelompoksix.siencehub.ui.screens.KerangkaAplikasi
import com.kelompoksix.siencehub.ui.screens.LoginScreen
import com.kelompoksix.siencehub.ui.screens.QuizResultScreen
import com.kelompoksix.siencehub.ui.screens.QuizReviewScreen
import com.kelompoksix.siencehub.ui.screens.QuizScreen
import com.kelompoksix.siencehub.ui.screens.SignupScreen
import com.kelompoksix.siencehub.ui.screens.SplashScreen
import com.kelompoksix.siencehub.ui.screens.WelcomeScreen
import com.kelompoksix.siencehub.utils.UserSessionManager

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        }
    ) {

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
                    navController.navigate(Routes.HOME) {
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
        // RUTE UTAMA: KERANGKA APLIKASI
        // ==========================================
        composable(Routes.HOME) {
            val context = LocalContext.current
            KerangkaAplikasi(
                onLogout = {
                    UserSessionManager.clearSession(context)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onStartQuiz = { subjectName ->
                    navController.navigate(Routes.createQuizRoute(subjectName))
                }
            )
        }

        // ==========================================
        // RUTE KUIS & HASIL KUIS
        // ==========================================
        composable(
            route = Routes.QUIZ,
            arguments = listOf(
                navArgument("subjectName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""

            QuizScreen(
                subjectName = subjectName,
                onBackClick = {
                    navController.popBackStack()
                },
                onQuizFinished = { score, total, correct, wrong ->
                    navController.navigate(Routes.createQuizResultRoute(score, total, correct, wrong, subjectName)) {
                        popUpTo(Routes.QUIZ) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.QUIZ_RESULT,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType },
                navArgument("correct") { type = NavType.IntType },
                navArgument("wrong") { type = NavType.IntType },
                navArgument("subjectName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val wrong = backStackEntry.arguments?.getInt("wrong") ?: 0
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""

            QuizResultScreen(
                score = score,
                totalQuestions = total,
                correctCount = correct,
                wrongCount = wrong,
                onHomeClick = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                },
                onReviewClick = {
                    navController.navigate(Routes.createQuizReviewRoute(subjectName))
                },
                onLeaderboardClick = {
                    // Navigasi ke halaman Leaderboard jika rutenya ada
                    // navController.navigate(Routes.LEADERBOARD)
                }
            )
        }

        // ==========================================
        // RUTE PEMBAHASAN KUIS
        // ==========================================
        composable(
            route = Routes.QUIZ_REVIEW,
            arguments = listOf(
                navArgument("subjectName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
            QuizReviewScreen(
                subjectName = subjectName,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}