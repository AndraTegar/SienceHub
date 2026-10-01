package com.kelompoksix.siencehub.data.repositories

import androidx.compose.runtime.mutableStateListOf
import com.kelompoksix.siencehub.data.models.UserRank

object LeaderboardRepository {
    // Menggunakan mutableStateListOf agar UI Compose otomatis terbarui saat skor berubah
    private val daftarLeaderboard = mutableStateListOf(
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

    // Tetap menggunakan nama fungsi milikmu
    fun getDaftarLeaderboard(): List<UserRank> {
        return daftarLeaderboard
    }

    // Fungsi untuk menambah skor pengguna "You" setelah selesai kuis
    fun tambahSkorUser(skorTambahan: Int) {
        val indexMe = daftarLeaderboard.indexOfFirst { it.isMe }

        if (indexMe != -1) {
            val userMe = daftarLeaderboard[indexMe]
            val skorBaru = userMe.poin + skorTambahan

            // 1. Update skor pengguna
            daftarLeaderboard[indexMe] = userMe.copy(poin = skorBaru)

            // 2. Urutkan ulang dari skor tertinggi ke terendah
            val listDiurutkan = daftarLeaderboard.sortedByDescending { it.poin }

            // 3. Perbarui daftar dan hitung ulang peringkat (rank 1, 2, 3, dst.)
            daftarLeaderboard.clear()
            listDiurutkan.forEachIndexed { index, user ->
                daftarLeaderboard.add(
                    user.copy(rank = index + 1)
                )
            }
        }
    }
}