package com.kelompoksix.siencehub.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val LEADERBOARD = "leaderboard"

    // Rute Kuis
    const val QUIZ = "quiz/{subjectName}"
    fun createQuizRoute(subjectName: String): String = "quiz/$subjectName"

    // Rute Hasil Kuis (Diperbarui dengan parameter score, total, correct, wrong, dan subjectName)
    const val QUIZ_RESULT = "quiz_result/{score}/{total}/{correct}/{wrong}/{subjectName}"
    fun createQuizResultRoute(
        score: Int,
        total: Int,
        correct: Int,
        wrong: Int,
        subjectName: String
    ): String = "quiz_result/$score/$total/$correct/$wrong/$subjectName"

    // Rute Pembahasan Kuis
    const val QUIZ_REVIEW = "quiz_review/{subjectName}"
    fun createQuizReviewRoute(subjectName: String): String = "quiz_review/$subjectName"
}