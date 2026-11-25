package com.kelompoksix.siencehub.data.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kelompoksix.siencehub.data.models.LearningStats
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object LocalLearningRepository {
    private const val DATABASE_NAME = "sciencehub_learning.db"
    private const val DATABASE_VERSION = 2
    private const val CHAPTER_XP = 20
    private const val CORRECT_ANSWER_XP = 10

    private class LearningDatabase(context: Context) :
        SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE chapter_completions (
                    user_id TEXT NOT NULL,
                    topic_id TEXT NOT NULL,
                    chapter_id INTEGER NOT NULL,
                    completed_at TEXT NOT NULL,
                    experience_points INTEGER NOT NULL,
                    PRIMARY KEY (user_id, topic_id, chapter_id)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE learning_days (
                    user_id TEXT NOT NULL,
                    activity_date TEXT NOT NULL,
                    PRIMARY KEY (user_id, activity_date)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE quiz_attempts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id TEXT NOT NULL,
                    subject_name TEXT NOT NULL,
                    score INTEGER NOT NULL,
                    total_score INTEGER NOT NULL,
                    correct_answers INTEGER NOT NULL,
                    experience_points INTEGER NOT NULL,
                    completed_at TEXT NOT NULL
                )
                """.trimIndent()
            )
            createTopicVisitsTable(db)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (oldVersion < 2) createTopicVisitsTable(db)
        }

        private fun createTopicVisitsTable(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS topic_visits (
                    user_id TEXT NOT NULL,
                    topic_id TEXT NOT NULL,
                    last_opened_at INTEGER NOT NULL,
                    PRIMARY KEY (user_id, topic_id)
                )
                """.trimIndent()
            )
        }
    }

    @Synchronized
    fun recordTopicOpened(context: Context, userId: String, topicId: String) {
        val dbHelper = LearningDatabase(context)
        try {
            dbHelper.writableDatabase.insertWithOnConflict(
                "topic_visits",
                null,
                android.content.ContentValues().apply {
                    put("user_id", normalizeUserId(userId))
                    put("topic_id", topicId)
                    put("last_opened_at", System.currentTimeMillis())
                },
                SQLiteDatabase.CONFLICT_REPLACE
            )
        } finally {
            dbHelper.close()
        }
    }

    @Synchronized
    fun completeChapter(context: Context, userId: String, topicId: String, chapterId: Int): Boolean {
        val normalizedUserId = normalizeUserId(userId)
        val today = LocalDate.now().toString()
        val dbHelper = LearningDatabase(context)
        val db = dbHelper.writableDatabase
        var wasNewlyCompleted = false

        db.beginTransaction()
        try {
            val insertedId = db.insertWithOnConflict(
                "chapter_completions",
                null,
                android.content.ContentValues().apply {
                    put("user_id", normalizedUserId)
                    put("topic_id", topicId)
                    put("chapter_id", chapterId)
                    put("completed_at", today)
                    put("experience_points", CHAPTER_XP)
                },
                SQLiteDatabase.CONFLICT_IGNORE
            )
            wasNewlyCompleted = insertedId != -1L
            recordLearningDay(db, normalizedUserId, today)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            dbHelper.close()
        }
        return wasNewlyCompleted
    }

    @Synchronized
    fun recordQuizResult(
        context: Context,
        userId: String,
        subjectName: String,
        score: Int,
        totalScore: Int,
        correctAnswers: Int
    ) {
        val normalizedUserId = normalizeUserId(userId)
        val today = LocalDate.now().toString()
        val experiencePoints = correctAnswers.coerceAtLeast(0) * CORRECT_ANSWER_XP
        val dbHelper = LearningDatabase(context)
        val db = dbHelper.writableDatabase

        db.beginTransaction()
        try {
            db.execSQL(
                """
                INSERT INTO quiz_attempts
                    (user_id, subject_name, score, total_score, correct_answers, experience_points, completed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                arrayOf<Any?>(
                    normalizedUserId,
                    subjectName.trim(),
                    score.coerceAtLeast(0),
                    totalScore.coerceAtLeast(0),
                    correctAnswers.coerceAtLeast(0),
                    experiencePoints,
                    today
                )
            )
            val matchingTopic = MateriRepository.getDaftarTopik().find {
                it.kategori.equals(subjectName.trim(), ignoreCase = true) ||
                it.judul.contains(subjectName.trim(), ignoreCase = true) ||
                subjectName.contains(it.kategori, ignoreCase = true)
            }
            if (matchingTopic != null) {
                matchingTopic.daftarBab.forEach { bab ->
                    db.insertWithOnConflict(
                        "chapter_completions",
                        null,
                        android.content.ContentValues().apply {
                            put("user_id", normalizedUserId)
                            put("topic_id", matchingTopic.id)
                            put("chapter_id", bab.id)
                            put("completed_at", today)
                            put("experience_points", CHAPTER_XP)
                        },
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }
            recordLearningDay(db, normalizedUserId, today)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            dbHelper.close()
        }
    }

    @Synchronized
    fun getStats(context: Context, userId: String): LearningStats {
        val normalizedUserId = normalizeUserId(userId)
        val dbHelper = LearningDatabase(context)
        val db = dbHelper.readableDatabase
        try {
            val completedByTopic = mutableMapOf<String, MutableSet<Int>>()
            db.rawQuery(
                "SELECT topic_id, chapter_id FROM chapter_completions WHERE user_id = ?",
                arrayOf(normalizedUserId)
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    completedByTopic.getOrPut(cursor.getString(0)) { mutableSetOf() }
                        .add(cursor.getInt(1))
                }
            }

            val recentlyOpenedTopicIds = mutableListOf<String>()
            db.rawQuery(
                "SELECT topic_id FROM topic_visits WHERE user_id = ? ORDER BY last_opened_at DESC",
                arrayOf(normalizedUserId)
            ).use { cursor ->
                while (cursor.moveToNext()) recentlyOpenedTopicIds += cursor.getString(0)
            }

            var chapterXp = 0
            db.rawQuery(
                "SELECT COALESCE(SUM(experience_points), 0) FROM chapter_completions WHERE user_id = ?",
                arrayOf(normalizedUserId)
            ).use { cursor ->
                if (cursor.moveToFirst()) chapterXp = cursor.getInt(0)
            }

            var quizXp = 0
            var correctAnswers = 0
            val perfectSubjects = mutableSetOf<String>()
            db.rawQuery(
                """
                SELECT COALESCE(SUM(experience_points), 0), COALESCE(SUM(correct_answers), 0)
                FROM quiz_attempts WHERE user_id = ?
                """.trimIndent(),
                arrayOf(normalizedUserId)
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    quizXp = cursor.getInt(0)
                    correctAnswers = cursor.getInt(1)
                }
            }
            db.rawQuery(
                """
                SELECT DISTINCT LOWER(subject_name) FROM quiz_attempts
                WHERE user_id = ? AND score > 0
                """.trimIndent(),
                arrayOf(normalizedUserId)
            ).use { cursor ->
                while (cursor.moveToNext()) perfectSubjects += cursor.getString(0).trim()
            }

            val activityDates = mutableListOf<LocalDate>()
            db.rawQuery(
                "SELECT activity_date FROM learning_days WHERE user_id = ? ORDER BY activity_date DESC",
                arrayOf(normalizedUserId)
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    activityDates += LocalDate.parse(cursor.getString(0))
                }
            }

            return LearningStats(
                experiencePoints = chapterXp + quizXp,
                streakDays = calculateCurrentStreak(activityDates),
                completedChaptersByTopic = completedByTopic.mapValues { it.value.toSet() },
                recentlyOpenedTopicIds = recentlyOpenedTopicIds,
                completedChapterCount = completedByTopic.values.sumOf { it.size },
                correctQuizAnswers = correctAnswers,
                perfectQuizSubjects = perfectSubjects
            )
        } finally {
            dbHelper.close()
        }
    }

    private fun recordLearningDay(db: SQLiteDatabase, userId: String, date: String) {
        db.execSQL(
            "INSERT OR IGNORE INTO learning_days (user_id, activity_date) VALUES (?, ?)",
            arrayOf(userId, date)
        )
    }

    private fun calculateCurrentStreak(activityDates: List<LocalDate>): Int {
        if (activityDates.isEmpty()) return 0
        val today = LocalDate.now()
        val mostRecent = activityDates.first()
        if (ChronoUnit.DAYS.between(mostRecent, today) > 1) return 0

        var streak = 1
        for (index in 1 until activityDates.size) {
            if (ChronoUnit.DAYS.between(activityDates[index], activityDates[index - 1]) != 1L) break
            streak++
        }
        return streak
    }

    private fun normalizeUserId(userId: String): String =
        userId.trim().lowercase().ifBlank { "local-user" }
}
