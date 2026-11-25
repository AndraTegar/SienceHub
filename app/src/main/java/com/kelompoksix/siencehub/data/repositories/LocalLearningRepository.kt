package com.kelompoksix.siencehub.data.repositories

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kelompoksix.siencehub.data.models.LearningStats
import com.kelompoksix.siencehub.data.models.TipeNotifikasi
import com.kelompoksix.siencehub.utils.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object LocalLearningRepository {
    private const val DATABASE_NAME = "sciencehub_learning.db"
    private const val DATABASE_VERSION = 2
    const val CHAPTER_XP = 25
    const val CORRECT_ANSWER_XP = 20

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _learningStatsFlow = MutableStateFlow(LearningStats.EMPTY)
    val learningStatsFlow: StateFlow<LearningStats> = _learningStatsFlow.asStateFlow()

    private var syncedCloudUid: String? = null

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

    fun resolveUserId(context: Context): String {
        val uid = auth.currentUser?.uid?.takeIf { it.isNotBlank() }
        if (uid != null) return uid.lowercase()
        val email = UserSessionManager.getUserEmail(context)
        return normalizeUserId(email)
    }

    @Synchronized
    fun refreshStats(context: Context, userId: String = resolveUserId(context)): LearningStats {
        val stats = getStats(context, userId)
        _learningStatsFlow.value = stats
        syncCloudDataIfNeeded(context, userId)
        return stats
    }

    private fun syncCloudDataIfNeeded(context: Context, userId: String) {
        val currentUser = auth.currentUser ?: return
        if (syncedCloudUid == currentUser.uid) return
        syncedCloudUid = currentUser.uid

        val appContext = context.applicationContext
        firestore.collection("users")
            .document(currentUser.uid)
            .collection("chapter_completions")
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val normalizedUserId = normalizeUserId(userId)
                    val dbHelper = LearningDatabase(appContext)
                    val db = dbHelper.writableDatabase
                    db.beginTransaction()
                    try {
                        snapshot.documents.forEach { doc ->
                            val topicId = doc.getString("topicId") ?: return@forEach
                            val chapterId = (doc.getLong("chapterId") ?: return@forEach).toInt()
                            val completedAt = doc.getString("completedAt") ?: LocalDate.now().toString()
                            val xp = (doc.getLong("xp") ?: CHAPTER_XP.toLong()).toInt()
                            db.insertWithOnConflict(
                                "chapter_completions",
                                null,
                                ContentValues().apply {
                                    put("user_id", normalizedUserId)
                                    put("topic_id", topicId)
                                    put("chapter_id", chapterId)
                                    put("completed_at", completedAt)
                                    put("experience_points", xp)
                                },
                                SQLiteDatabase.CONFLICT_IGNORE
                            )
                            recordLearningDay(db, normalizedUserId, completedAt)
                        }
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                        dbHelper.close()
                    }
                    val updated = getStats(appContext, userId)
                    _learningStatsFlow.value = updated
                    updateAllSubjectProgressFromStats(appContext, updated)
                }
            }
    }

    fun resetCloudSyncCache() {
        syncedCloudUid = null
        _learningStatsFlow.value = LearningStats.EMPTY
    }

    @Synchronized
    fun recordTopicOpened(context: Context, userId: String = resolveUserId(context), topicId: String) {
        val normalizedUserId = normalizeUserId(userId)
        val dbHelper = LearningDatabase(context)
        try {
            dbHelper.writableDatabase.insertWithOnConflict(
                "topic_visits",
                null,
                ContentValues().apply {
                    put("user_id", normalizedUserId)
                    put("topic_id", topicId)
                    put("last_opened_at", System.currentTimeMillis())
                },
                SQLiteDatabase.CONFLICT_REPLACE
            )
        } finally {
            dbHelper.close()
        }
        _learningStatsFlow.value = getStats(context, normalizedUserId)
    }

    @Synchronized
    fun completeChapter(
        context: Context,
        userId: String = resolveUserId(context),
        topicId: String,
        chapterId: Int
    ): Boolean {
        val previousStats = getStats(context, userId)
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
                ContentValues().apply {
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

        val updatedStats = getStats(context, normalizedUserId)
        _learningStatsFlow.value = updatedStats

        val topic = MateriRepository.getDaftarTopik().find { it.id == topicId }
        if (topic != null && topic.daftarBab.isNotEmpty()) {
            val completedCount = updatedStats.completedChaptersByTopic[topicId]?.size ?: 0
            val ratio = completedCount.toFloat() / topic.daftarBab.size
            UserProgressRepository.updateSubjectProgress(context, topic.kategori, ratio)
        }

        if (wasNewlyCompleted) {
            LeaderboardRepository.tambahSkorUser(CHAPTER_XP)
            syncChapterCompletionToCloud(topicId, chapterId, today)
        }

        emitMilestoneNotifications(previousStats, updatedStats)
        return wasNewlyCompleted
    }

    private fun syncChapterCompletionToCloud(topicId: String, chapterId: Int, completedAt: String) {
        val currentUser = auth.currentUser ?: return
        val data = hashMapOf(
            "topicId" to topicId,
            "chapterId" to chapterId,
            "completedAt" to completedAt,
            "xp" to CHAPTER_XP,
            "timestamp" to System.currentTimeMillis()
        )
        firestore.collection("users")
            .document(currentUser.uid)
            .collection("chapter_completions")
            .document("${topicId}_$chapterId")
            .set(data, SetOptions.merge())
    }

    @Synchronized
    fun recordQuizResult(
        context: Context,
        userId: String = resolveUserId(context),
        subjectName: String,
        score: Int,
        totalScore: Int,
        correctAnswers: Int
    ) {
        val previousStats = getStats(context, userId)
        val normalizedUserId = normalizeUserId(userId)
        val normalizedSubject = MateriRepository.normalizeSubjectName(subjectName)
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
                    normalizedSubject,
                    score.coerceAtLeast(0),
                    totalScore.coerceAtLeast(0),
                    correctAnswers.coerceAtLeast(0),
                    experiencePoints,
                    today
                )
            )
            // Jika nilai sempurna (100%), seluruh bab di topik tersebut otomatis ditandai tuntas
            if (totalScore > 0 && score >= totalScore) {
                val matchingTopic = MateriRepository.findTopicBySubject(normalizedSubject)
                matchingTopic?.daftarBab?.forEach { bab ->
                    val inserted = db.insertWithOnConflict(
                        "chapter_completions",
                        null,
                        ContentValues().apply {
                            put("user_id", normalizedUserId)
                            put("topic_id", matchingTopic.id)
                            put("chapter_id", bab.id)
                            put("completed_at", today)
                            put("experience_points", CHAPTER_XP)
                        },
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                    if (inserted != -1L) {
                        syncChapterCompletionToCloud(matchingTopic.id, bab.id, today)
                    }
                }
            }
            recordLearningDay(db, normalizedUserId, today)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            dbHelper.close()
        }

        val updatedStats = getStats(context, normalizedUserId)
        _learningStatsFlow.value = updatedStats

        val quizRatio = if (totalScore > 0) score.toFloat() / totalScore.toFloat() else 0f
        val topic = MateriRepository.findTopicBySubject(normalizedSubject)
        val chapterRatio = if (topic != null && topic.daftarBab.isNotEmpty()) {
            (updatedStats.completedChaptersByTopic[topic.id]?.size ?: 0).toFloat() / topic.daftarBab.size
        } else {
            0f
        }
        UserProgressRepository.updateSubjectProgress(
            context = context,
            subjectName = normalizedSubject,
            progress = maxOf(quizRatio, chapterRatio)
        )

        emitMilestoneNotifications(previousStats, updatedStats)
    }

    private fun updateAllSubjectProgressFromStats(context: Context, stats: LearningStats) {
        MateriRepository.getDaftarTopik().forEach { topic ->
            if (topic.daftarBab.isNotEmpty()) {
                val ratio = (stats.completedChaptersByTopic[topic.id]?.size ?: 0).toFloat() / topic.daftarBab.size
                if (ratio > 0f) {
                    UserProgressRepository.updateSubjectProgress(context, topic.kategori, ratio)
                }
            }
        }
    }

    private fun emitMilestoneNotifications(before: LearningStats, after: LearningStats) {
        if (after.streakDays > before.streakDays) {
            NotifikasiRepository.tambahNotifikasi(
                judul = "Streak ${after.streakDays} Hari Aktif! 🔥",
                pesan = "Mantap! Kamu konsisten belajar sains ${after.streakDays} hari berturut-turut.",
                tipe = TipeNotifikasi.STREAK
            )
        }
        if (after.level > before.level) {
            NotifikasiRepository.tambahNotifikasi(
                judul = "Naik ke Level ${after.level} (${after.levelTitle})! ⭐",
                pesan = "Total XP kamu kini ${after.experiencePoints} XP. Terus tingkatkan pencapaianmu!",
                tipe = TipeNotifikasi.INFO
            )
        }
        if (before.completedChapterCount < 1 && after.completedChapterCount >= 1) {
            NotifikasiRepository.tambahNotifikasi(
                judul = "Pencapaian Terbuka: Langkah Pertama 🌟",
                pesan = "Selamat! Kamu baru saja menuntaskan bab materi pertamamu.",
                tipe = TipeNotifikasi.MATERI
            )
        }
        if (before.completedTopicCount < 1 && after.completedTopicCount >= 1) {
            NotifikasiRepository.tambahNotifikasi(
                judul = "Pencapaian Terbuka: Penjelajah Sains 🚀",
                pesan = "Luar biasa! Kamu berhasil menuntaskan 100% bab pada satu mata pelajaran.",
                tipe = TipeNotifikasi.MATERI
            )
        }
        val bioKimiaBefore = setOf("biologi", "kimia").all { req ->
            before.perfectQuizSubjects.any { it.equals(req, ignoreCase = true) }
        }
        val bioKimiaAfter = setOf("biologi", "kimia").all { req ->
            after.perfectQuizSubjects.any { it.equals(req, ignoreCase = true) }
        }
        if (!bioKimiaBefore && bioKimiaAfter) {
            NotifikasiRepository.tambahNotifikasi(
                judul = "Pencapaian Terbuka: Ilmuwan Gila 🧪",
                pesan = "Kamu meraih nilai sempurna pada kuis Biologi dan Kimia!",
                tipe = TipeNotifikasi.KUIS
            )
        }
        if (before.correctQuizAnswers < 50 && after.correctQuizAnswers >= 50) {
            NotifikasiRepository.tambahNotifikasi(
                judul = "Pencapaian Terbuka: Raja Kuis 🏆",
                pesan = "Hebat! Kamu telah menjawab 50 soal kuis dengan benar.",
                tipe = TipeNotifikasi.KUIS
            )
        }
    }

    @Synchronized
    fun getStats(context: Context, userId: String = resolveUserId(context)): LearningStats {
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
                SELECT DISTINCT subject_name FROM quiz_attempts
                WHERE user_id = ? AND total_score > 0 AND score >= total_score
                """.trimIndent(),
                arrayOf(normalizedUserId)
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    val norm = MateriRepository.normalizeSubjectName(cursor.getString(0)).lowercase()
                    perfectSubjects += norm
                }
            }

            // Fallback: gabungkan juga dengan QuizHistoryRepository jika ada riwayat kuis sebelumnya dari Cloud
            val cloudHistory = QuizHistoryRepository.quizHistoryFlow.value
            if (quizXp == 0 && cloudHistory.isNotEmpty()) {
                quizXp = cloudHistory.sumOf { it.score.coerceAtLeast(0) }
                correctAnswers = cloudHistory.sumOf { it.correctCount.coerceAtLeast(0) }
            }
            cloudHistory.forEach { item ->
                if (item.totalQuestions > 0 && item.correctCount >= item.totalQuestions) {
                    perfectSubjects += MateriRepository.normalizeSubjectName(item.subjectName).lowercase()
                }
            }

            val activityDates = mutableListOf<LocalDate>()
            db.rawQuery(
                "SELECT activity_date FROM learning_days WHERE user_id = ? ORDER BY activity_date DESC",
                arrayOf(normalizedUserId)
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    runCatching { LocalDate.parse(cursor.getString(0)) }
                        .getOrNull()
                        ?.let { activityDates += it }
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
        val sortedDistinct = activityDates.distinct().sortedDescending()
        val today = LocalDate.now()
        val mostRecent = sortedDistinct.first()
        if (ChronoUnit.DAYS.between(mostRecent, today) > 1) return 0

        var streak = 1
        for (index in 1 until sortedDistinct.size) {
            if (ChronoUnit.DAYS.between(sortedDistinct[index], sortedDistinct[index - 1]) != 1L) break
            streak++
        }
        return streak
    }

    private fun normalizeUserId(userId: String): String =
        userId.trim().lowercase().ifBlank { "local-user" }
}
