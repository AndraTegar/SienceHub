package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
fun EksperimenScaffold(
    judul: String,
    onBackClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color(0xFFFFC94D))) {
        Spacer(Modifier.height(44.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp), contentAlignment = Alignment.Center) {
            Row(
                Modifier.fillMaxWidth().height(48.dp)
                    .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(50.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, "Kembali", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Text(judul, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(28.dp))
        Surface(
            Modifier.fillMaxWidth().weight(1f),
            color = Color(0xFFF9F7FF),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = content
            )
        }
    }
}

@Composable
fun PilihanChip(teks: String, terpilih: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.clip(RoundedCornerShape(50.dp))
            .background(if (terpilih) Color(0xFF7D9078) else Color(0xFFC9DDF5))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(teks, color = if (terpilih) Color.White else Color.Black, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}