package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.models.UserRank

object LeaderboardRepository {
    fun getDaftarLeaderboard(): List<UserRank> {
        return listOf(
            UserRank(1, "Asep", 43),
            UserRank(2, "Meghan Jessica", 40),
            UserRank(3, "Bimo", 38),
            UserRank(4, "Marsha Fisher", 36),
            UserRank(5, "Juanita Cormier", 35),
            UserRank(6, "You", 34, isMe = true), // Posisi Pengguna saat ini
            UserRank(7, "Tamara Schmidt", 33),
            UserRank(8, "Ricardo Veum", 32),
            UserRank(9, "Gary Sanford", 31),
            UserRank(10, "Becky Bartell", 30),
            UserRank(11, "Amdra Tegar", 30),
            UserRank(12, "Siti Rahma", 28)
        )
    }
}