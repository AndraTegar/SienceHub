package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.components.CustomBottomNavigation

@Composable
fun MateriScreen() {
    Scaffold(
        // Set parameter activeMenu ke "Lesson"
        bottomBar = { CustomBottomNavigation(activeMenu = "Lesson") },
        containerColor = Color(0xFF7A8D7B)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Teks Header
            Text(
                text = "Halo, Djokowi",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Mau belajar apa hari ini?",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Grid Konten 2 Kolom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Kolom Kiri
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SubjectCard("FISIKA", Color(0xFF38383D), 200.dp)
                    SubjectCard("BIOLOGI", Color(0xFFD4E674), 220.dp)
                    SubjectCard("", Color(0xFFFFC996), 180.dp) // Card bawah kiri
                }

                // Kolom Kanan
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SubjectCard("KIMIA", Color(0xFF8CD4CA), 160.dp)
                    SubjectCard("ASTRONOMI", Color(0xFFF9C84F), 220.dp)
                    SubjectCard("", Color(0xFF00C3A9), 180.dp) // Card bawah kanan
                }
            }
        }
    }
}

// Komponen Card Satuan yang bisa dipanggil berulang
@Composable
fun SubjectCard(title: String, bgColor: Color, height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(16.dp)
    ) {
        // Area tengah ini nanti bisa kamu isi dengan komponen Image()
        // untuk memasukkan ikon aset PNG/SVG dari Figma.

        if (title.isNotEmpty()) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}