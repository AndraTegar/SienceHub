package com.kelompoksix.siencehub.data.models

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
        get() = experiencePoints / 100 + 1

    fun progressFor(topic: TopikMateri): Float {
        if (topic.daftarBab.isEmpty()) return 0f
        val chapterRatio = ((completedChaptersByTopic[topic.id]?.size ?: 0).toFloat() / topic.daftarBab.size)
        val subjectKey = when {
            topic.kategori.contains("Fisika", ignoreCase = true) || topic.judul.contains("Fisika", ignoreCase = true) -> "Fisika"
            topic.kategori.contains("Biologi", ignoreCase = true) || topic.judul.contains("Biologi", ignoreCase = true) -> "Biologi"
            topic.kategori.contains("Kimia", ignoreCase = true) || topic.judul.contains("Kimia", ignoreCase = true) -> "Kimia"
            else -> topic.kategori
        }
        val repositoryProgress = UserProgressRepository.getProgressForSubject(subjectKey)
        val isSubjectPerfect = perfectQuizSubjects.any { it.contains(subjectKey, ignoreCase = true) }
        val finalProgress = maxOf(chapterRatio, repositoryProgress, if (isSubjectPerfect) 1.0f else 0.0f)
        return finalProgress.coerceIn(0f, 1f)
    }

    companion object {
        val EMPTY = LearningStats()
    }
}
