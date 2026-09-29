package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfilScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToAchievement: () -> Unit, // Tambahan rute baru
    onNavigateToLeaderboard: () -> Unit
) {
    val sageGreen = Color(0xFF7A8B76)
    val lightBlue = Color(0xFFE3F0FF)

    Box(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.45f).background(sageGreen))

        Surface(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.65f).align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            color = Color(0xFFFAFAFA)
        ) {}

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(4.dp, Color.White, CircleShape)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Ridho Surya Saputra", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text(text = "ridho@gmail.com", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))

            Spacer(modifier = Modifier.height(32.dp))

            // Barisan Tombol Melayang
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                MenuKotakFresh(icon = Icons.Default.BarChart, bgColor = lightBlue, onClick = onNavigateToLeaderboard)
                Spacer(modifier = Modifier.width(20.dp))
                MenuKotakFresh(icon = Icons.Default.GridView, bgColor = lightBlue, onClick = onNavigateToAchievement)
                Spacer(modifier = Modifier.width(20.dp))
                MenuKotakFresh(icon = Icons.Default.Settings, bgColor = lightBlue, onClick = onNavigateToSettings)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Kartu Level & Streak
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadgeFresh(icon = Icons.Default.Star, value = "1", label = "Level", iconColor = Color(0xFF00B0FF), bgColor = Color(0xFFE1F5FE))
                    Box(modifier = Modifier.height(45.dp).width(1.5.dp).background(Color.LightGray.copy(alpha = 0.5f)))
                    StatusBadgeFresh(icon = Icons.Default.LocalFireDepartment, value = "6", label = "Streak", iconColor = Color(0xFFFF6D00), bgColor = Color(0xFFFFF3E0))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Kartu Statistik
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
                Text(text = "Pencapaian Kamu", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().height(70.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).background(Color(0xFFE8ECE7), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFF7A8B76))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("3 Materi Diselesaikan", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuKotakFresh(icon: androidx.compose.ui.graphics.vector.ImageVector, bgColor: Color, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp), color = bgColor, shadowElevation = 4.dp,
        modifier = Modifier.size(75.dp).clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF556052), modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun StatusBadgeFresh(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, iconColor: Color, bgColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(48.dp).background(bgColor, CircleShape), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color.Black)
            Text(text = label, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}