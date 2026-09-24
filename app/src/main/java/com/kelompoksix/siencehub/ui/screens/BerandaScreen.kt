package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.ui.components.KartuKategori
import com.kelompoksix.siencehub.ui.components.KartuMateri

@Composable
fun BerandaScreen(
    onNavigateToMateri: () -> Unit = {}
) {
    val daftarMateri = MateriRepository.getDaftarMateri()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Hero / Welcome
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Selamat Datang di SienceHub! 👋",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Eksplorasi konsep sains, pelajari materi terbaru, dan uji pemahamanmu secara interaktif.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Kategori Sains Quick Menu
        item {
            Column {
                Text(
                    text = "Kategori Sains",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KartuKategori(
                        judul = "Fisika",
                        icon = Icons.Default.Science,
                        onClick = onNavigateToMateri,
                        modifier = Modifier.weight(1f)
                    )
                    KartuKategori(
                        judul = "Biologi",
                        icon = Icons.Default.Eco,
                        onClick = onNavigateToMateri,
                        modifier = Modifier.weight(1f)
                    )
                    KartuKategori(
                        judul = "Kimia",
                        icon = Icons.Default.WaterDrop,
                        onClick = onNavigateToMateri,
                        modifier = Modifier.weight(1f)
                    )
                    KartuKategori(
                        judul = "Astronomi",
                        icon = Icons.Default.Public,
                        onClick = onNavigateToMateri,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section Title: Materi Pilihan
        item {
            Text(
                text = "Materi Pilihan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // List of Materi Cards
        items(daftarMateri) { materi ->
            KartuMateri(
                materi = materi,
                onClick = onNavigateToMateri
            )
        }
    }
}
