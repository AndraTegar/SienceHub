package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.UserRank
import com.kelompoksix.siencehub.data.repositories.LeaderboardRepository

@Composable
fun LeaderboardScreen(
    onBackClick: () -> Unit
) {
    val headerColor = MaterialTheme.colorScheme.primary
    val daftarUser = LeaderboardRepository.getDaftarLeaderboard()

    val topThree = daftarUser.take(3)
    val remainingUsers = if (daftarUser.size > 3) daftarUser.drop(3) else emptyList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(headerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(50))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Kembali",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Leaderboard",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                if (topThree.size > 1) {
                    PodiumItem(user = topThree[1])
                }
                if (topThree.isNotEmpty()) {
                    PodiumItem(user = topThree[0])
                }
                if (topThree.size > 2) {
                    PodiumItem(user = topThree[2])
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(remainingUsers) { user ->
                        LeaderboardItemCard(user = user)
                    }
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumItem(user: UserRank) {
    val ringColor = when (user.rank) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFE0E0E0)
        else -> Color(0xFFCD7F32)
    }

    val verticalOffset = when (user.rank) {
        1 -> (-28.dp)
        2 -> (-10.dp)
        else -> 0.dp
    }

    val avatarSize = if (user.rank == 1) 76.dp else 64.dp
    val iconSize = if (user.rank == 1) 38.dp else 30.dp

    Column(
        modifier = Modifier
            .width(100.dp)
            .offset(y = verticalOffset),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.height(avatarSize + 20.dp)
        ) {
            if (user.rank == 1) {
                Text(
                    text = "👑",
                    fontSize = 22.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-4.dp))
                )
            }

            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .align(Alignment.BottomCenter)
                    .border(3.5.dp, ringColor, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(avatarSize - 8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 8.dp)
                    .background(ringColor, CircleShape)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${user.rank}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = user.nama,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "⚡ ${user.poin} pts",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LeaderboardItemCard(user: UserRank) {
    val backgroundColor = if (user.isMe) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (user.isMe) MaterialTheme.colorScheme.primary else Color.Transparent
    val textColor = MaterialTheme.colorScheme.onSurface

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (user.isMe) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(if (user.isMe) 1.5.dp else 0.dp, borderColor, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${user.rank}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.width(30.dp)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (user.isMe) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (user.isMe) Icons.Default.EmojiEvents else Icons.Default.Person,
                    contentDescription = null,
                    tint = if (user.isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = if (user.isMe) "You (Kamu)" else user.nama,
                fontSize = 15.sp,
                fontWeight = if (user.isMe) FontWeight.ExtraBold else FontWeight.Medium,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${user.poin} pts",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}
