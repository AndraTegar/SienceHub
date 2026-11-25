package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.UserRank
import com.kelompoksix.siencehub.data.repositories.LeaderboardRepository
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SageDark
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

@Composable
fun LeaderboardScreen(
    onBackClick: () -> Unit
) {
    val metrics = rememberResponsiveMetrics()

    DisposableEffect(Unit) {
        LeaderboardRepository.listenRealtimeLeaderboard()
        onDispose {
            LeaderboardRepository.stopListening()
        }
    }

    val daftarUser by LeaderboardRepository.leaderboardFlow.collectAsState()
    val topThree = daftarUser.take(3)
    val remainingUsers = if (daftarUser.size > 3) daftarUser.drop(3) else emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onBackClick,
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Kembali",
                        tint = SagePrimary,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Leaderboard",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Area Podium 3 Besar (Responsif dengan weight(1f) agar presisi di semua lebar layar)
        ResponsiveContentContainer(maxWidth = 480.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = if (metrics.isShortScreen) 8.dp else 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                    if (topThree.size > 1) {
                        PodiumItem(user = topThree[1])
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                    if (topThree.isNotEmpty()) {
                        PodiumItem(user = topThree[0])
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                    if (topThree.size > 2) {
                        PodiumItem(user = topThree[2])
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SurfaceOffWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 680.dp) {
                if (daftarUser.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SagePrimary)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding(),
                        contentPadding = PaddingValues(
                            horizontal = metrics.horizontalPadding,
                            vertical = 20.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(remainingUsers) { user ->
                            LeaderboardItemCard(user = user)
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
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
        2 -> Color(0xFFE2E8F0)
        else -> Color(0xFFCD7F32)
    }

    val verticalOffset = when (user.rank) {
        1 -> (-20).dp
        2 -> (-6).dp
        else -> 0.dp
    }

    val avatarSize = if (user.rank == 1) 74.dp else 62.dp
    val iconSize = if (user.rank == 1) 36.dp else 28.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
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
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-4).dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .align(Alignment.BottomCenter)
                    .border(3.dp, ringColor, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(avatarSize - 8.dp)
                        .clip(CircleShape)
                        .background(SageSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = SagePrimary,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 8.dp)
                    .background(ringColor, CircleShape)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${user.rank}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = user.nama,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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
    val backgroundColor = if (user.isMe) SageSoft else SurfaceWhite
    val borderColor = if (user.isMe) SagePrimary else BorderSubtle
    val textColor = TextPrimary

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (user.isMe) 2.dp else 0.dp),
        border = BorderStroke(if (user.isMe) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${user.rank}",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (user.isMe) SageDark else TextSecondary,
                modifier = Modifier.width(30.dp)
            )

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (user.isMe) SagePrimary.copy(alpha = 0.18f) else SurfaceOffWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (user.isMe) Icons.Default.EmojiEvents else Icons.Default.Person,
                    contentDescription = null,
                    tint = if (user.isMe) SagePrimary else TextSecondary,
                    modifier = Modifier.size(19.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = user.nama,
                fontSize = 14.5.sp,
                fontWeight = if (user.isMe) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${user.poin} pts",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (user.isMe) SageDark else TextPrimary
            )
        }
    }
}
