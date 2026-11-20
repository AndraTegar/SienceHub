package com.kelompoksix.siencehub.data.repositories

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.kelompoksix.siencehub.data.models.UserRank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LeaderboardRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Standby fallback mock data jika offline / Firestore belum terisi
    private val mockLeaderboard = listOf(
        UserRank(1, "Asep Saepulloh", 120),
        UserRank(2, "Meghan Jessica", 100),
        UserRank(3, "Bimo Setyo", 85),
        UserRank(4, "Marsha Fisher", 70),
        UserRank(5, "Juanita Cormier", 65),
        UserRank(6, "You (Kamu)", 50, isMe = true),
        UserRank(7, "Tamara Schmidt", 45),
        UserRank(8, "Ricardo Veum", 40)
    )

    private val _leaderboardFlow = MutableStateFlow<List<UserRank>>(mockLeaderboard)
    val leaderboardFlow: StateFlow<List<UserRank>> = _leaderboardFlow.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    // Mengambil data Leaderboard Real-Time dari Cloud Firestore
    fun listenRealtimeLeaderboard() {
        // Matikan listener lama jika ada
        listenerRegistration?.remove()

        listenerRegistration = db.collection("users")
            .orderBy("xp", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.isEmpty) {
                    // Fallback ke mock data jika terjadi error/kosong
                    return@addSnapshotListener
                }

                val currentUid = auth.currentUser?.uid
                val currentEmail = auth.currentUser?.email

                val listRank = snapshot.documents.mapIndexedNotNull { index, doc ->
                    val uid = doc.getString("uid") ?: doc.id
                    val name = doc.getString("displayName")
                        ?.ifBlank { null }
                        ?: doc.getString("email")?.substringBefore("@")
                        ?: "Siswa SienceHub"
                    val xp = (doc.getLong("xp") ?: doc.getLong("poin") ?: 0L).toInt()
                    val email = doc.getString("email") ?: ""

                    val isMe = (currentUid != null && currentUid == uid) ||
                            (currentEmail != null && currentEmail.equals(email, ignoreCase = true))

                    UserRank(
                        rank = index + 1,
                        nama = if (isMe) "$name (Kamu)" else name,
                        poin = xp,
                        isMe = isMe
                    )
                }

                if (listRank.isNotEmpty()) {
                    _leaderboardFlow.value = listRank
                }
            }
    }

    // Menghentikan listener saat tidak digunakan lagi
    fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    // Menambah skor/XP pengguna saat menyelesaikan kuis
    fun tambahSkorUser(skorTambahan: Int, onSuccess: () -> Unit = {}) {
        val currentUser = auth.currentUser ?: return

        val userRef = db.collection("users").document(currentUser.uid)

        userRef.get().addOnSuccessListener { snapshot ->
            val xpSekarang = (snapshot.getLong("xp") ?: 0L).toInt()
            val xpBaru = xpSekarang + skorTambahan
            val levelBaru = (xpBaru / 100) + 1

            val dataUpdate = hashMapOf<String, Any>(
                "uid" to currentUser.uid,
                "displayName" to (currentUser.displayName ?: snapshot.getString("displayName") ?: "Siswa"),
                "email" to (currentUser.email ?: ""),
                "xp" to xpBaru,
                "level" to levelBaru,
                "lastUpdated" to System.currentTimeMillis()
            )

            userRef.set(dataUpdate, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    onSuccess()
                }
        }
    }

    fun getDaftarLeaderboard(): List<UserRank> {
        return _leaderboardFlow.value
    }
}
