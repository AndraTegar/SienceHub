package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.R

class WavyShapeWelcome : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            lineTo(0f, size.height * 0.75f)
            cubicTo(
                size.width * 0.35f, size.height * 0.95f,
                size.width * 0.65f, size.height * 0.75f,
                size.width, size.height * 0.9f
            )
            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun WelcomeScreen(
    onContinueClick: () -> Unit = {}
) {
    val sageGreen = Color(0xFF7A8B76)
    val darkText = Color(0xFF333333)

    // Daftar kalimat random seputar sains yang akan dipilih secara acak setiap halaman dibuka
    val scienceQuotes = remember {
        listOf(
            "Jelajahi keajaiban sains dan raih pengetahuan baru setiap hari.",
            "Sains adalah kunci utama untuk membuka rahasia alam semesta.",
            "Temukan berbagai wawasan, fakta, dan eksperimen menarik di sini.",
            "Mulailah petualangan ilmiahmu bersama komunitas ScienceHub.",
            "PRABOWO LOVE TEDDY."
        )
    }
    // Mengambil satu kalimat secara acak
    val randomQuote = remember { scienceQuotes.random() }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Background lengkungan atas dengan tekstur
        Image(
            painter = painterResource(id = R.drawable.texture_bg),
            contentDescription = "Background Wavy",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .clip(WavyShapeWelcome()),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Teks "ayo ayo" sudah dihapus dan diganti ruang bersih

            Text(
                text = "Welcome",
                color = darkText,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Menampilkan kalimat random yang dinamis
            Text(
                text = randomQuote,
                color = Color.Gray,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Continue",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
                IconButton(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(color = sageGreen, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Continue",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}