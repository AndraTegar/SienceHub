package com.kelompoksix.siencehub.data.models

import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.data.repositories.UserProgressRepository

data class LearningStats(
    val experiencePoints: Int = 0,
    val streakDays: Int = 0,
    val completedChaptersByTopic: Map<String, Set<Int>> = emptyMap(),
    val recentlyOpenedTopicIds: List<String> = emptyList(),
    val completedChapterCount: Int = 0,
    val correctQuizAnswers: Int = 0,
    val perfectQuizSubjects: Set<String> = emptySet()
) {
    val level: Int
        get() = (experiencePoints / 100) + 1

    val xpInCurrentLevel: Int
        get() = experiencePoints % 100

    val xpForNextLevel: Int
        get() = 100

    val levelTitle: String
        get() = when {
            level >= 10 -> "Profesor Sains"
            level >= 6 -> "Ilmuwan Mahir"
            level >= 4 -> "Peneliti Muda"
            level >= 2 -> "Penjelajah Muda"
            else -> "Pemula Sains"
        }

    val completedTopicCount: Int
        get() = MateriRepository.getDaftarTopik().count { progressFor(it) >= 1.0f }

    fun progressFor(topic: TopikMateri): Float {
        if (topic.daftarBab.isEmpty()) return 0f
        val chapterRatio = (completedChaptersByTopic[topic.id]?.size ?: 0).toFloat() / topic.daftarBab.size
        val subjectKey = MateriRepository.normalizeSubjectName(topic.kategori)
        val repositoryProgress = UserProgressRepository.getProgressForSubject(subjectKey)
        val isSubjectPerfect = perfectQuizSubjects.any {
            MateriRepository.normalizeSubjectName(it).equals(subjectKey, ignoreCase = true)
        }
        val finalProgress = maxOf(chapterRatio, repositoryProgress, if (isSubjectPerfect) 1.0f else 0.0f)
        return finalProgress.coerceIn(0f, 1f)
    }

    companion object {
        val EMPTY = LearningStats()
    }
}
