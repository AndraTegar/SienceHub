package com.kelompoksix.siencehub.data.models

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

    fun progressFor(topic: TopikMateri): Float =
        if (topic.daftarBab.isEmpty()) 0f
        else ((completedChaptersByTopic[topic.id]?.size ?: 0).toFloat() / topic.daftarBab.size)
            .coerceIn(0f, 1f)

    companion object {
        val EMPTY = LearningStats()
    }
}
