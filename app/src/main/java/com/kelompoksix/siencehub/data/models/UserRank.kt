package com.kelompoksix.siencehub.data.models

data class UserRank(
    val rank: Int,
    val nama: String,
    val poin: Int,
    val isMe: Boolean = false
)