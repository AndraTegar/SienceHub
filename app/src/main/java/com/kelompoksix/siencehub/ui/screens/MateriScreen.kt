package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.repositories.KategoriDataProvider
import com.kelompoksix.siencehub.ui.components.KartuKategori
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import com.kelompoksix.siencehub.utils.UserSessionManager

@Composable
fun MateriScreen(
    onMateriClick: (String) -> Unit = {}
) {
    val daftarKategori = KategoriDataProvider.getDaftarKategori()
    val metrics = rememberResponsiveMetrics()

    val context = LocalContext.current
    val userName = remember(context) {
        UserSessionManager.getUserName(context).ifBlank { "Pengguna ScienceHub" }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        ResponsiveContentContainer(maxWidth = 840.dp) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = metrics.horizontalPadding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Halo, $userName",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mau belajar apa hari ini?",
                    fontSize = 14.5.sp,
                    color = Color.White.copy(alpha = 0.86f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(minSize = 152.dp),
                    contentPadding = PaddingValues(bottom = 112.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalItemSpacing = 14.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(daftarKategori) { kategori ->
                        KartuKategori(
                            item = kategori,
                            onClick = { onMateriClick(kategori.judul) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MateriScreenPreview() {
    MateriScreen()
}
