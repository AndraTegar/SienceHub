package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val GreenNav = Color(0xFF7A8D7B)

@Composable
fun CustomBottomNavigation(activeMenu: String = "Home") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Icon
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (activeMenu == "Home") Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    tint = if (activeMenu == "Home") GreenNav else Color.Gray
                )
                if (activeMenu == "Home") {
                    Text("Home", fontSize = 12.sp, color = GreenNav, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.width(20.dp).height(3.dp).clip(RoundedCornerShape(50)).background(GreenNav))
                }
            }

            // Lesson Icon
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (activeMenu == "Lesson") Icons.Filled.Book else Icons.Outlined.Book,
                    contentDescription = "Lesson",
                    tint = if (activeMenu == "Lesson") GreenNav else Color.Gray
                )
                if (activeMenu == "Lesson") {
                    Text("Lesson", fontSize = 12.sp, color = GreenNav, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.width(20.dp).height(3.dp).clip(RoundedCornerShape(50)).background(GreenNav))
                }
            }

            // Profile Icon
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.Person, contentDescription = "Profile", tint = Color.Gray)
                if (activeMenu == "Profile") {
                    Text("Profile", fontSize = 12.sp, color = GreenNav, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.width(20.dp).height(3.dp).clip(RoundedCornerShape(50)).background(GreenNav))
                }
            }
        }
    }
}