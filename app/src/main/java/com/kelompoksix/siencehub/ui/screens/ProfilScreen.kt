package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.theme.GreenBackground
import com.kelompoksix.siencehub.ui.theme.GreenLight
import com.kelompoksix.siencehub.ui.theme.Cream90
import com.kelompoksix.siencehub.ui.theme.CreamLight
import com.kelompoksix.siencehub.ui.theme.GrayColor

@Composable
fun ProfilScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToAchievement: () -> Unit,
    onNavigateToLeaderboard: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamLight)
    ) {
        // Header Atas (Background Hijau)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.38f)
                .background(GreenBackground)
        )

        // Bottom Sheet Surface (Background Krem Utama)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.68f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
            color = CreamLight
        ) {}

        // Konten Utama
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Foto Profil
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .border(4.dp, CreamLight, CircleShape)
                    .clip(CircleShape)
                    .background(GreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto Profil",
                    modifier = Modifier.size(60.dp),
                    tint = GreenBackground
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Identitas Pengguna
            Text(
                text = "Ridho Surya Saputra",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CreamLight
            )
            Text(
                text = "ridho@gmail.com",
                fontSize = 14.sp,
                color = CreamLight.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Barisan Tombol Navigasi (Menu Kotak)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                MenuKotakFresh(
                    icon = Icons.Default.BarChart,
                    bgColor = Cream90,
                    onClick = onNavigateToLeaderboard
                )
                Spacer(modifier = Modifier.width(16.dp))
                MenuKotakFresh(
                    icon = Icons.Default.GridView,
                    bgColor = Cream90,
                    onClick = onNavigateToAchievement
                )
                Spacer(modifier = Modifier.width(16.dp))
                MenuKotakFresh(
                    icon = Icons.Default.Settings,
                    bgColor = Cream90,
                    onClick = onNavigateToSettings
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Kartu Ringkasan Level & Streak
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadgeFresh(
                        icon = Icons.Default.Star,
                        value = "1",
                        label = "Level",
                        iconColor = GreenBackground,
                        bgColor = GreenLight.copy(alpha = 0.35f)
                    )
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                            .background(GrayColor.copy(alpha = 0.15f))
                    )
                    StatusBadgeFresh(
                        icon = Icons.Default.LocalFireDepartment,
                        value = "6",
                        label = "Streak",
                        iconColor = Color(0xFFE65100),
                        bgColor = Color(0xFFFFF3E0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kartu Statistik & Pencapaian
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            ) {
                Text(
                    text = "Pencapaian Kamu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrayColor
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(GreenLight.copy(alpha = 0.35f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = GreenBackground
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "3 Materi Diselesaikan",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = GrayColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuKotakFresh(
    icon: ImageVector,
    bgColor: Color,
    iconTint: Color = GrayColor,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        shadowElevation = 3.dp,
        modifier = Modifier.size(70.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun StatusBadgeFresh(
    icon: ImageVector,
    value: String,
    label: String,
    iconColor: Color,
    bgColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = GrayColor
            )
            Text(
                text = label,
                fontSize = 13.sp,
                color = GrayColor.copy(alpha = 0.65f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}