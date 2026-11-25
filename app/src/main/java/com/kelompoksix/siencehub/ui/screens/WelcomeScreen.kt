package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.R
import com.kelompoksix.siencehub.ui.components.WavyShape
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics

@Composable
fun WelcomeScreen(onContinueClick: () -> Unit = {}) {
    val welcomeMessages = listOf(
        "Jelajahi keajaiban sains dan raih pengetahuan baru setiap hari.",
        "Sains adalah kunci utama untuk membuka rahasia alam semesta.",
        "Temukan berbagai wawasan, fakta, dan eksperimen menarik di sini.",
        "Mulailah petualangan ilmiahmu bersama komunitas ScienceHub."
    )

    val randomMessage = remember { welcomeMessages.random() }
    val metrics = rememberResponsiveMetrics()
    val imageHeightFraction = if (metrics.isShortScreen) 0.48f else 0.58f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceOffWhite)
    ) {
        Image(
            painter = painterResource(id = R.drawable.texture_bg),
            contentDescription = "Background Topography",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(imageHeightFraction)
                .clip(WavyShape())
        )

        ResponsiveContentContainer(
            maxWidth = 540.dp,
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = if (metrics.isTablet) 40.dp else 28.dp,
                        vertical = if (metrics.isShortScreen) 28.dp else 40.dp
                    ),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "Welcome",
                    color = TextPrimary,
                    fontSize = if (metrics.isShortScreen) 34.sp else 40.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = randomMessage,
                    color = TextSecondary,
                    fontSize = 14.5.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(if (metrics.isShortScreen) 32.dp else 48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mulai Sekarang",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SagePrimary
                    )

                    Surface(
                        onClick = onContinueClick,
                        shape = CircleShape,
                        color = SagePrimary,
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_forward),
                                contentDescription = "Continue",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
