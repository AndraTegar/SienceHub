package com.kelompoksix.siencehub.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class MenuEksperimen(
    val id: String,
    val mapel: String,
    val judul: String,
    val tersedia: Boolean
)

private val daftarEksperimen = listOf(
    MenuEksperimen("fisika_newton2", "Fisika", "Simulasi Hukum II Newton", true),
    MenuEksperimen("astronomi_gravitasi", "Astronomi", "Gravitasi di berbagai planet", true),
    MenuEksperimen("kimia_ph", "Kimia", "Uji pH dengan indikator", true),
    MenuEksperimen("matematika_timbangan", "Matematika", "Timbangan persamaan", true)
)

@Composable
fun EksperimenScreen(onBackClick: () -> Unit, onPilih: (String) -> Unit) {
    val context = LocalContext.current
    val kuning = Color(0xFFFFC94D)
    val biruKartu = Color(0xFFC9DDF5)
    val biruTeks = Color(0xFF2F5FE3)

    Column(Modifier.fillMaxSize().background(kuning)) {
        Spacer(Modifier.height(44.dp))
        Box(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier.fillMaxWidth().height(48.dp)
                    .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(50.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, "Kembali", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Text(
                "EKSPERIMEN VIRTUAL", color = Color.White, fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(28.dp))

        Surface(
            Modifier.fillMaxWidth().weight(1f),
            color = Color(0xFFF9F7FF),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                Modifier.padding(horizontal = 32.dp).padding(top = 56.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                daftarEksperimen.forEach { m ->
                    Row(
                        Modifier.fillMaxWidth()
                            .alpha(if (m.tersedia) 1f else 0.6f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(biruKartu)
                            .clickable {
                                if (m.tersedia) onPilih(m.id)
                                else Toast.makeText(context, "Segera hadir", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(m.mapel, color = biruTeks, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(m.judul, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Color.White)
                    }
                }
            }
        }
    }
}