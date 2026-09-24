package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.CustomBottomNavigation

// Warna Tema
val GreenBackground = Color(0xFF7A8D7B)
val BeigeColor = Color(0xFFD8CFC0)
val DarkGreenPill = Color(0xFF90A190)
val LightBackground = Color(0xFFF7F7FA)
val PlaceholderGray = Color(0xFFD9D9D9)

@Composable
fun BerandaScreen() {
    Scaffold(
        bottomBar = { CustomBottomNavigation() },
        containerColor = GreenBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header (Search, Profile, Notif)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Blue)
                    )
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(50))
                            .background(BeigeColor)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Search", color = Color.Gray, fontSize = 14.sp)
                    }
                    Icon(
                        Icons.Outlined.Notifications,
                        contentDescription = "Notification",
                        tint = Color.White
                    )
                }

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Stats Pill (Level & Streak)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(50))
                        .background(DarkGreenPill),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("Level", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    VerticalDivider(
                        color = Color.DarkGray.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxHeight(0.6f)
                    )
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("Streak", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Overlapping Card & Main Content
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 80.dp)
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(LightBackground)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 100.dp, start = 24.dp, end = 24.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(PlaceholderGray))
                                Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(PlaceholderGray))
                                Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(PlaceholderGray))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(PlaceholderGray))
                                Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(PlaceholderGray))
                                Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(PlaceholderGray))
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(16.dp)).background(PlaceholderGray))
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(16.dp)).background(PlaceholderGray))
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(BeigeColor)
                    )
                }
            }
        }
    }
}